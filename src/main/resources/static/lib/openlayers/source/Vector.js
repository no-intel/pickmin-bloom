/**
 * @module ol/source/Vector
 */

import Collection from '../Collection.js';
import CollectionEventType from '../CollectionEventType.js';
import Event from '../events/Event.js';
import EventType from '../events/EventType.js';
import ObjectEventType from '../ObjectEventType.js';
import RBush from '../structs/RBush.js';
import RenderFeature from '../render/Feature.js';
import Source from './Source.js';
import VectorEventType from './VectorEventType.js';
import {TRUE, VOID} from '../functions.js';
import {all as allStrategy} from '../loadingstrategy.js';
import {assert} from '../asserts.js';
import {containsExtent, equals, wrapAndSliceX} from '../extent.js';
import {extend} from '../array.js';
import {getUid} from '../util.js';
import {isEmpty} from '../obj.js';
import {listen, unlistenByKey} from '../events.js';
import {xhr} from '../featureloader.js';

/**
 * A function that takes an {@link module:ol/extent~Extent} and a resolution as arguments, and
 * returns an array of {@link module:ol/extent~Extent} with the extents to load. Usually this
 * is one of the standard {@link module:ol/loadingstrategy} strategies.
 *
 * @typedef {function(import("../extent.js").Extent, number, import("../proj/Projection.js").default): Array<import("../extent.js").Extent>} LoadingStrategy
 * @api
 */

/**
 * @classdesc
 * Events emitted by {@link module:ol/source/Vector~VectorSource} instances are instances of this
 * type.
 * @template {import("../Feature.js").FeatureLike} [FeatureType=import("../Feature.js").default]
 */
export class VectorSourceEvent extends Event {
  /**
   * @param {string} type Type.
   * @param {FeatureType} [feature] Feature.
   * @param {Array<FeatureType>} [features] Features.
   */
  constructor(type, feature, features) {
    super(type);

    /**
     * The added or removed feature for the `ADDFEATURE` and `REMOVEFEATURE` events, `undefined` otherwise.
     * @type {FeatureType|undefined}
     * @api
     */
    this.feature = feature;

    /**
     * The loaded features for the `FEATURESLOADED` event, `undefined` otherwise.
     * @type {Array<FeatureType>|undefined}
     * @api
     */
    this.features = features;
  }
}

/***
 * @template {import("../Feature.js").FeatureLike} [T=import("../Feature.js").default]
 * @typedef {T extends RenderFeature ? T|Array<T> : T} FeatureClassOrArrayOfRenderFeatures
 */

/***
 * @template Return
 * @template {import("../Feature.js").FeatureLike} [FeatureType=import("../Feature.js").default]
 * @typedef {import("../Observable").OnSignature<import("../Observable").EventTypes, import("../events/Event.js").default, Return> &
 *   import("../Observable").OnSignature<import("../ObjectEventType").Types, import("../Object").ObjectEvent, Return> &
 *   import("../Observable").OnSignature<import("./VectorEventType").VectorSourceEventTypes, VectorSourceEvent<FeatureType>, Return> &
 *   import("../Observable").CombinedOnSignature<import("../Observable").EventTypes|import("../ObjectEventType").Types|
 *     import("./VectorEventType").VectorSourceEventTypes, Return>} VectorSourceOnSignature
 */

/**
 * @template {import("../Feature.js").FeatureLike} [FeatureType=import("../Feature.js").default]
 * @typedef {Object} Options
 * @property {import("./Source.js").AttributionLike} [attributions] Attributions.
 * @property {Array<FeatureType>|Collection<FeatureType>} [features]
 * Features. If provided as {@link module:ol/Collection~Collection}, the features in the source
 * and the collection will stay in sync.
 * @property {import("../format/Feature.js").default<FeatureType>} [format] The feature format used by the XHR
 * feature loader when `url` is set. Required if `url` is set, otherwise ignored.
 * @property {import("../featureloader.js").FeatureLoader<FeatureType>} [loader]
 * The loader function used to load features, from a remote source for example.
 * If this is not set and `url` is set, the source will create and use an XHR
 * feature loader. The `'featuresloadend'` and `'featuresloaderror'` events
 * will only fire if the `success` and `failure` callbacks are used.
 *
 * Example:
 *
 * ```js
 * import Vector from 'ol/source/vectorLayer.js';
 * import GeoJSON from 'ol/format/GeoJSON.js';
 * import {bbox} from 'ol/loadingstrategy.js';
 *
 * const vectorSource = new Vector({
 *   format: new GeoJSON(),
 *   loader: function(extent, resolution, projection, success, failure) {
 *      const proj = projection.getCode();
 *      const url = 'https://ahocevar.com/geoserver/wfs?service=WFS&' +
 *          'version=1.1.0&request=GetFeature&typename=osm:water_areas&' +
 *          'outputFormat=application/json&srsname=' + proj + '&' +
 *          'bbox=' + extent.join(',') + ',' + proj;
 *      const xhr = new XMLHttpRequest();
 *      xhr.open('GET', url);
 *      const onError = function() {
 *        vectorSource.removeLoadedExtent(extent);
 *        failure();
 *      }
 *      xhr.onerror = onError;
 *      xhr.onload = function() {
 *        if (xhr.status == 200) {
 *          const features = vectorSource.getFormat().readFeatures(xhr.responseText);
 *          vectorSource.addFeatures(features);
 *          success(features);
 *        } else {
 *          onError();
 *        }
 *      }
 *      xhr.send();
 *    },
 *    strategy: bbox,
 *  });
 * ```
 * @property {boolean} [overlaps=true] This source may have overlapping geometries.
 * Setting this to `false` (e.g. for sources with polygons that represent administrative
 * boundaries or TopoJSON sources) allows the renderer to optimise fill and
 * stroke operations.
 * @property {LoadingStrategy} [strategy] The loading strategy to use.
 * By default an {@link module:ol/loadingstrategy.all}
 * strategy is used, a one-off strategy which loads all features at once.
 * @property {string|import("../featureloader.js").FeatureUrlFunction} [url]
 * Setting this option instructs the source to load features using an XHR loader
 * (see {@link module:ol/featureloader.xhr}). Use a `string` and an
 * {@link module:ol/loadingstrategy.all} for a one-off download of all features from
 * the given URL. Use a {@link module:ol/featureloader~FeatureUrlFunction} to generate the url with
 * other loading strategies.
 * Requires `format` to be set as well.
 * When default XHR feature loader is provided, the features will
 * be transformed from the data projection to the view projection
 * during parsing. If your remote data source does not advertise its projection
 * properly, this transformation will be incorrect. For some formats, the
 * default projection (usually EPSG:4326) can be overridden by setting the
 * dataProjection constructor option on the format.
 * Note that if a source contains non-feature data, such as a GeoJSON geometry
 * or a KML NetworkLink, these will be ignored. Use a custom loader to load these.
 * @property {boolean} [useSpatialIndex=true]
 * By default, an RTree is used as spatial index. When features are removed and
 * added frequently, and the total number of features is low, setting this to
 * `false` may improve performance.
 *
 * Note that
 * {@link module:ol/source/Vector~VectorSource#getFeaturesInExtent},
 * {@link module:ol/source/Vector~VectorSource#getClosestFeatureToCoordinate} and
 * {@link module:ol/source/Vector~VectorSource#getExtent} cannot be used when `useSpatialIndex` is
 * set to `false`, and {@link module:ol/source/Vector~VectorSource#forEachFeatureInExtent} will loop
 * through all features.
 *
 * When set to `false`, the features will be maintained in an
 * {@link module:ol/Collection~Collection}, which can be retrieved through
 * {@link module:ol/source/Vector~VectorSource#getFeaturesCollection}.
 * @property {boolean} [wrapX=true] Wrap the world horizontally. For vector editing across the
 * -180° and 180° meridians to work properly, this should be set to `false`. The
 * resulting geometry coordinates will then exceed the world bounds.
 */

/**
 * @classdesc
 * Provides a source of features for vector layers. Vector features provided
 * by this source are suitable for editing. See {@link module:ol/source/VectorTile~VectorTile} for
 * vector data that is optimized for rendering.
 *
 * @fires VectorSourceEvent
 * @api
 * @template {import("../Feature.js").FeatureLike} [FeatureType=import("../Feature.js").default]
 */
class VectorSource extends Source {
  /**
   * @param {Options<FeatureType>} [options] Vector source options.
   */
  constructor(options) {
    options = options || {};

    super({
      attributions: options.attributions,
      interpolate: true,
      projection: undefined,
      state: 'ready',
      wrapX: options.wrapX !== undefined ? options.wrapX : true,
    });

    /***
     * @type {VectorSourceOnSignature<import("../events").EventsKey, FeatureType>}
     */
    this.on;

    /***
     * @type {VectorSourceOnSignature<import("../events").EventsKey, FeatureType>}
     */
    this.once;

    /***
     * @type {VectorSourceOnSignature<void>}
     */
    this.un;

    /**
     * @private
     * @type {import("../featureloader.js").FeatureLoader<FeatureType>}
     */
    this.loader_ = VOID;

    /**
     * @private
     * @type {import("../format/Feature.js").default<FeatureType>|null}
     */
    this.format_ = options.format || null;

    /**
     * @private
     * @type {boolean}
     */
    this.overlaps_ = options.overlaps === undefined ? true : options.overlaps;

    /**
     * @private
     * @type {string|import("../featureloader.js").FeatureUrlFunction|undefined}
     */
    this.url_ = options.url;

    if (options.loader !== undefined) {
      this.loader_ = options.loader;
    } else if (this.url_ !== undefined) {
      assert(this.format_, '`format` must be set when `url` is set');
      // create a XHR feature loader for "url" and "format"
      this.loader_ = xhr(this.url_, this.format_);
    }

    /**
     * @private
     * @type {LoadingStrategy}
     */
    this.strategy_ =
      options.strategy !== undefined ? options.strategy : allStrategy;

    const useSpatialIndex =
      options.useSpatialIndex !== undefined ? options.useSpatialIndex : true;

    /**
     * @private
     * @type {RBush<FeatureType>}
     */
    this.featuresRtree_ = useSpatialIndex ? new RBush() : null;

    /**
     * @private
     * @type {RBush<{extent: import("../extent.js").Extent}>}
     */
    this.loadedExtentsRtree_ = new RBush();

    /**
     * @type {number}
     * @private
     */
    this.loadingExtentsCount_ = 0;

    /**
     * @private
     * @type {!Object<string, FeatureType>}
     */
    this.nullGeometryFeatures_ = {};

    /**
     * A lookup of features by id (the return from feature.getId()).
     * @private
     * @type {!Object<string, import('../Feature.js').FeatureLike|Array<import('../Feature.js').FeatureLike>>}
     */
    this.idIndex_ = {};

    /**
     * A lookup of features by uid (using getUid(feature)).
     * @private
     * @type {!Object<string, FeatureType>}
     */
    this.uidIndex_ = {};

    /**
     * @private
     * @type {Object<string, Array<import("../events.js").EventsKey>>}
     */
    this.featureChangeKeys_ = {};

    /**
     * @private
     * @type {Collection<FeatureType>|null}
     */
    this.featuresCollection_ = null;

    /** @type {Collection<FeatureType>} */
    let collection;
    /** @type {Array<FeatureType>} */
    let features;
    if (Array.isArray(options.features)) {
      features = options.features;
    } else if (options.features) {
      collection = options.features;
      features = collection.getArray();
    }
    if (!useSpatialIndex && collection === undefined) {
      collection = new Collection(features);
    }
    if (features !== undefined) {
      this.addFeaturesInternal(features);
    }
    if (collection !== undefined) {
      this.bindFeaturesCollection_(collection);
    }
  }

  /**
   * Add a single feature to the source.  If you want to add a batch of features
   * at once, call {@link module:ol/source/Vector~VectorSource#addFeatures #addFeatures()}
   * instead. A feature will not be added to the source if feature with
   * the same id is already there. The reason for this behavior is to avoid
   * feature duplication when using bbox or tile loading strategies.
   * Note: this also applies if a {@link module:ol/Collection~Collection} is used for features,
   * meaning that if a feature with a duplicate id is added in the collection, it will
   * be removed from it right away.
   * @param {FeatureType} feature Feature to add.
   * @api
   */
  addFeature(feature) {
    this.addFeatureInternal(feature);
    this.changed();
  }

  /**
   * Add a feature without firing a `change` event.
   * @param {FeatureType} feature Feature.
   * @protected
   */
  addFeatureInternal(feature) {
    const featureKey = getUid(feature);

    if (!this.addToIndex_(featureKey, feature)) {
      if (this.featuresCollection_) {
        this.featuresCollection_.remove(feature);
      }
      return;
    }

    this.setupChangeEvents_(featureKey, feature);

    const geometry = feature.getGeometry();
    if (geometry) {
      const extent = geometry.getExtent();
      if (this.featuresRtree_) {
        this.featuresRtree_.insert(extent, feature);
      }
    } else {
      this.nullGeometryFeatures_[featureKey] = feature;
    }

    this.dispatchEvent(
      new VectorSourceEvent(VectorEventType.ADDFEATURE, feature),
    );
  }

  /**
   * @param {string} featureKey Unique identifier for the feature.
   * @param {FeatureType} feature The feature.
   * @private
   */
  setupChangeEvents_(featureKey, feature) {
    if (feature instanceof RenderFeature) {
      return;
    }
    this.featureChangeKeys_[featureKey] = [
      listen(feature, EventType.CHANGE, this.handleFeatureChange_, this),
      listen(
        feature,
        ObjectEventType.PROPERTYCHANGE,
        this.handleFeatureChange_,
        this,
      ),
    ];
  }

  /**
   * @param {string} featureKey Unique identifier for the feature.
   * @param {FeatureType} feature The feature.
   * @return {boolean} The feature is "valid", in the sense that it is also a
   *     candidate for insertion into the Rtree.
   * @private
   */
  addToIndex_(featureKey, feature) {
    let valid = true;
    if (feature.getId() !== undefined) {
      const id = String(feature.getId());
      if (!(id in this.idIndex_)) {
        this.idIndex_[id] = feature;
      } else if (feature instanceof RenderFeature) {
        const indexedFeature = this.idIndex_[id];
        if (!(indexedFeature instanceof RenderFeature)) {
          valid = false;
        } else if (!Array.isArray(indexedFeature)) {
          this.idIndex_[id] = [indexedFeature, feature];
        } else {
          indexedFeature.push(feature);
        }
      } else {
        valid = false;
      }
    }
    if (valid) {
      assert(
        !(featureKey in this.uidIndex_),
        'The passed `feature` was already added to the source',
      );
      this.uidIndex_[featureKey] = feature;
    }
    return valid;
  }

  /**
   * Add a batch of features to the source.
   * @param {Array<FeatureType>} features Features to add.
   * @api
   */
  addFeatures(features) {
    this.addFeaturesInternal(features);
    this.changed();
  }

  /**
   * Add features without firing a `change` event.
   * @param {Array<FeatureType>} features Features.
   * @protected
   */
  addFeaturesInternal(features) {
    const extents = [];
    /** @type {Array<FeatureType>} */
    const newFeatures = [];
    /** @type {Array<FeatureType>} */
    const geometryFeatures = [];

    for (let i = 0, length = features.length; i < length; i++) {
      const feature = features[i];
      const featureKey = getUid(feature);
      if (this.addToIndex_(featureKey, feature)) {
        newFeatures.push(feature);
      }
    }

    for (let i = 0, length = newFeatures.length; i < length; i++) {
      const feature = newFeatures[i];
      const featureKey = getUid(feature);
      this.setupChangeEvents_(featureKey, feature);

      const geometry = feature.getGeometry();
      if (geometry) {
        const extent = geometry.getExtent();
        extents.push(extent);
        geometryFeatures.push(feature);
      } else {
        this.nullGeometryFeatures_[featureKey] = feature;
      }
    }
    if (this.featuresRtree_) {
      this.featuresRtree_.load(extents, geometryFeatures);
    }

    if (this.hasListener(VectorEventType.ADDFEATURE)) {
      for (let i = 0, length = newFeatures.length; i < length; i++) {
        this.dispatchEvent(
          new VectorSourceEvent(VectorEventType.ADDFEATURE, newFeatures[i]),
        );
      }
    }
  }

  /**
   * @param {!Collection<FeatureType>} collection Collection.
   * @private
   */
  bindFeaturesCollection_(collection) {
    let modifyingCollection = false;
    this.addEventListener(
      VectorEventType.ADDFEATURE,
      /**
       * @param {VectorSourceEvent<FeatureType>} evt The vector source event
       */
      function (evt) {
        if (!modifyingCollection) {
          modifyingCollection = true;
          collection.push(evt.feature);
          modifyingCollection = false;
        }
      },
    );
    this.addEventListener(
      VectorEventType.REMOVEFEATURE,
      /**
       * @param {VectorSourceEvent<FeatureType>} evt The vector source event
       */
      function (evt) {
        if (!modifyingCollection) {
          modifyingCollection = true;
          collection.remove(evt.feature);
          modifyingCollection = false;
        }
      },
    );
    collection.addEventListener(
      CollectionEventType.ADD,
      /**
       * @param {import("../Collection.js").CollectionEvent<FeatureType>} evt The collection event
       */
      (evt) => {
        if (!modifyingCollection) {
          modifyingCollection = true;
          this.addFeature(evt.element);
          modifyingCollection = false;
        }
      },
    );
    collection.addEventListener(
      CollectionEventType.REMOVE,
      /**
       * @param {import("../Collection.js").CollectionEvent<FeatureType>} evt The collection event
       */
      (evt) => {
        if (!modifyingCollection) {
          modifyingCollection = true;
          this.removeFeature(evt.element);
          modifyingCollection = false;
        }
      },
    );
    this.featuresCollection_ = collection;
  }

  /**
   * Remove all features from the source.
   * @param {boolean} [fast] Skip dispatching of {@link module:ol/source/Vector.VectorSourceEvent#event:removefeature} events.
   * @api
   */
  clear(fast) {
    if (fast) {
      for (const featureId in this.featureChangeKeys_) {
        const keys = this.featureChangeKeys_[featureId];
        keys.forEach(unlistenByKey);
      }
      if (!this.featuresCollection_) {
        this.featureChangeKeys_ = {};
        this.idIndex_ = {};
        this.uidIndex_ = {};
      }
    } else {
      if (this.featuresRtree_) {
        this.featuresRtree_.forEach((feature) => {
          this.removeFeatureInternal(feature);
        });
        for (const id in this.nullGeometryFeatures_) {
          this.removeFeatureInternal(this.nullGeometryFeatures_[id]);
        }
      }
    }
    if (this.featuresCollection_) {
      this.featuresCollection_.clear();
    }

    if (this.featuresRtree_) {
      this.featuresRtree_.clear();
    }
    this.nullGeometryFeatures_ = {};

    const clearEvent = new VectorSourceEvent(VectorEventType.CLEAR);
    this.dispatchEvent(clearEvent);
    this.changed();
  }

  /**
   * Iterate through all features on the source, calling the provided callback
   * with each one.  If the callback returns any "truthy" value, iteration will
   * stop and the function will return the same value.
   * Note: this function only iterate through the feature that have a defined geometry.
   *
   * @param {function(FeatureType): T} callback Called with each feature
   *     on the source.  Return a truthy value to stop iteration.
   * @return {T|undefined} The return value from the last call to the callback.
   * @template T
   * @api
   */
  forEachFeature(callback) {
    if (this.featuresRtree_) {
      return this.featuresRtree_.forEach(callback);
    }
    if (this.featuresCollection_) {
      this.featuresCollection_.forEach(callback);
    }
  }

  /**
   * Iterate through all features whose geometries contain the provided
   * coordinate, calling the callback with each feature.  If the callback returns
   * a "truthy" value, iteration will stop and the function will return the same
   * value.
   *
   * For {@link module:ol/render/Feature~RenderFeature} features, the callback will be
   * called for all features.
   *
   * @param {import("../coordinate.js").Coordinate} coordinate Coordinate.
   * @param {function(FeatureType): T} callback Called with each feature
   *     whose goemetry contains the provided coordinate.
   * @return {T|undefined} The return value from the last call to the callback.
   * @template T
   */
  forEachFeatureAtCoordinateDirect(coordinate, callback) {
    const extent = [coordinate[0], coordinate[1], coordinate[0], coordinate[1]];
    return this.forEachFeatureInExtent(extent, function (feature) {
      const geometry = feature.getGeometry();
      if (
        geometry instanceof RenderFeature ||
        geometry.intersectsCoordinate(coordinate)
      ) {
        return callback(feature);
      }
      return undefined;
    });
  }

  /**
   * Iterate through all features whose bounding box intersects the provided
   * extent (note that the feature's geometry may not intersect the extent),
   * calling the callback with each feature.  If the callback returns a "truthy"
   * value, iteration will stop and the function will return the same value.
   *
   * If you are interested in features whose geometry intersects an extent, call
   * the {@link module:ol/source/Vector~VectorSource#forEachFeatureIntersectingExtent #forEachFeatureIntersectingExtent()} method instead.
   *
   * When `useSpatialIndex` is set to false, this method will loop through all
   * features, equivalent to {@link module:ol/source/Vector~VectorSource#forEachFeature #forEachFeature()}.
   *
   * @param {import("../extent.js").Extent} extent Extent.
   * @param {function(FeatureType): T} callback Called with each feature
   *     whose bounding box intersects the provided extent.
   * @return {T|undefined} The return value from the last call to the callback.
   * @template T
   * @api
   */
  forEachFeatureInExtent(extent, callback) {
    if (this.featuresRtree_) {
      return this.featuresRtree_.forEachInExtent(extent, callback);
    }
    if (this.featuresCollection_) {
      this.featuresCollection_.forEach(callback);
    }
  }

  /**
   * Iterate through all features whose geometry intersects the provided extent,
   * calling the callback with each feature.  If the callback returns a "truthy"
   * value, iteration will stop and the function will return the same value.
   *
   * If you only want to test for bounding box intersection, call the
   * {@link module:ol/source/Vector~VectorSource#forEachFeatureInExtent #forEachFeatureInExtent()} method instead.
   *
   * @param {import("../extent.js").Extent} extent Extent.
   * @param {function(FeatureType): T} callback Called with each feature
   *     whose geometry intersects the provided extent.
   * @return {T|undefined} The return value from the last call to the callback.
   * @template T
   * @api
   */
  forEachFeatureIntersectingExtent(extent, callback) {
    return this.forEachFeatureInExtent(
      extent,
      /**
       * @param {FeatureType} feature Feature.
       * @return {T|undefined} The return value from the last call to the callback.
       */
      function (feature) {
        const geometry = feature.getGeometry();
        if (
          geometry instanceof RenderFeature ||
          geometry.intersectsExtent(extent)
        ) {
          const result = callback(feature);
          if (result) {
            return result;
          }
        }
      },
    );
  }

  /**
   * Get the features collection associated with this source. Will be `null`
   * unless the source was configured with `useSpatialIndex` set to `false`, or
   * with a {@link module:ol/Collection~Collection} as `features`.
   * @return {Collection<FeatureType>|null} The collection of features.
   * @api
   */
  getFeaturesCollection() {
    return this.featuresCollection_;
  }

  /**
   * Get a snapshot of the features currently on the source in random order. The returned array
   * is a copy, the features are references to the features in the source.
   * @return {Array<FeatureType>} Features.
   * @api
   */
  getFeatures() {
    let features;
    if (this.featuresCollection_) {
      features = this.featuresCollection_.getArray().slice(0);
    } else if (this.featuresRtree_) {
      features = this.featuresRtree_.getAll();
      if (!isEmpty(this.nullGeometryFeatures_)) {
        extend(features, Object.values(this.nullGeometryFeatures_));
      }
    }
    return features;
  }

  /**
   * Get all features whose geometry intersects the provided coordinate.
   * @param {import("../coordinate.js").Coordinate} coordinate Coordinate.
   * @return {Array<FeatureType>} Features.
   * @api
   */
  getFeaturesAtCoordinate(coordinate) {
    /** @type {Array<FeatureType>} */
    const features = [];
    this.forEachFeatureAtCoordinateDirect(coordinate, function (feature) {
      features.push(feature);
    });
    return features;
  }

  /**
   * Get all features whose bounding box intersects the provided extent.  Note that this returns an array of
   * all features intersecting the given extent in random order (so it may include
   * features whose geometries do not intersect the extent).
   *
   * When `useSpatialIndex` is set to false, this method will return all
   * features.
   *
   * @param {import("../extent.js").Extent} extent Extent.
   * @param {import("../proj/Projection.js").default} [projection] Include features
   * where `extent` exceeds the x-axis bounds of `projection` and wraps around the world.
   * @return {Array<FeatureType>} Features.
   * @api
   */
  getFeaturesInExtent(extent, projection) {
    if (this.featuresRtree_) {
      const multiWorld = projection && projection.canWrapX() && this.getWrapX();

      if (!multiWorld) {
        return this.featuresRtree_.getInExtent(extent);
      }

      const extents = wrapAndSliceX(extent, projection);

      return [].concat(
        ...extents.map((anExtent) => this.featuresRtree_.getInExtent(anExtent)),
      );
    }
    if (this.featuresCollection_) {
      return this.featuresCollection_.getArray().slice(0);
    }
    return [];
  }

  /**
   * Get the closest feature to the provided coordinate.
   *
   * This method is not available when the source is configured with
   * `useSpatialIndex` set to `false` and the features in this source are of type
   * {@link module:ol/Feature~Feature}.
   * @param {import("../coordinate.js").Coordinate} coordinate Coordinate.
   * @param {function(FeatureType):boolean} [filter] Feature filter function.
   *     The filter function will receive one argument, the {@link module:ol/Feature~Feature feature}
   *     and it should return a boolean value. By default, no filtering is made.
   * @return {FeatureType} Closest feature.
   * @api
   */
  getClosestFeatureToCoordinate(coordinate, filter) {
    // Find the closest feature using branch and bound.  We start searching an
    // infinite extent, and find the distance from the first feature found.  This
    // becomes the closest feature.  We then compute a smaller extent which any
    // closer feature must intersect.  We continue searching with this smaller
    // extent, trying to find a closer feature.  Every time we find a closer
    // feature, we update the extent being searched so that any even closer
    // feature must intersect it.  We continue until we run out of features.
    const x = coordinate[0];
    const y = coordinate[1];
    let closestFeature = null;
    const closestPoint = [NaN, NaN];
    let minSquaredDistance = Infinity;
    const extent = [-Infinity, -Infinity, Infinity, Infinity];
    filter = filter ? filter : TRUE;
    this.featuresRtree_.forEachInExtent(
      extent,
      /**
       * @param {FeatureType} feature Feature.
       */
      function (feature) {
        if (filter(feature)) {
          const geometry = feature.getGeometry();
          const previousMinSquaredDistance = minSquaredDistance;
          minSquaredDistance =
            geometry instanceof RenderFeature
              ? 0
              : geometry.closestPointXY(x, y, closestPoint, minSquaredDistance);
          if (minSquaredDistance < previousMinSquaredDistance) {
            closestFeature = feature;
            // This is sneaky.  Reduce the extent that it is currently being
            // searched while the R-Tree traversal using this same extent object
            // is still in progress.  This is safe because the new extent is
            // strictly contained by the old extent.
            const minDistance = Math.sqrt(minSquaredDistance);
            extent[0] = x - minDistance;
            extent[1] = y - minDistance;
            extent[2] = x + minDistance;
            extent[3] = y + minDistance;
          }
        }
      },
    );
    return closestFeature;
  }

  /**
   * Get the extent of the features currently in the source.
   *
   * This method is not available when the source is configured with
   * `useSpatialIndex` set to `false`.
   * @param {import("../extent.js").Extent} [extent] Destination extent. If provided, no new extent
   *     will be created. Instead, that extent's coordinates will be overwritten.
   * @return {import("../extent.js").Extent} Extent.
   * @api
   */
  getExtent(extent) {
    return this.featuresRtree_.getExtent(extent);
  }

  /**
   * Get a feature by its identifier (the value returned by feature.getId()). When `RenderFeature`s
   * are used, `getFeatureById()` can return an array of `RenderFeature`s. This allows for handling
   * of `GeometryCollection` geometries, where format readers create one `RenderFeature` per
   * `GeometryCollection` member.
   * Note that the index treats string and numeric identifiers as the same.  So
   * `source.getFeatureById(2)` will return a feature with id `'2'` or `2`.
   *
   * @param {string|number} id Feature identifier.
   * @return {FeatureClassOrArrayOfRenderFeatures<FeatureType>|null} The feature (or `null` if not found).
   * @api
   */
  getFeatureById(id) {
    const feature = this.idIndex_[id.toString()];
    return feature !== undefined
      ? /** @type {FeatureClassOrArrayOfRenderFeatures<FeatureType>} */ (
          feature
        )
      : null;
  }

  /**
   * Get a feature by its internal unique identifier (using `getUid`).
   *
   * @param {string} uid Feature identifier.
   * @return {FeatureType|null} The feature (or `null` if not found).
   */
  getFeatureByUid(uid) {
    const feature = this.uidIndex_[uid];
    return feature !== undefined ? feature : null;
  }

  /**
   * Get the format associated with this source.
   *
   * @return {import("../format/Feature.js").default<FeatureType>|null}} The feature format.
   * @api
   */
  getFormat() {
    return this.format_;
  }

  /**
   * @return {boolean} The source can have overlapping geometries.
   */
  getOverlaps() {
    return this.overlaps_;
  }

  /**
   * Get the url associated with this source.
   *
   * @return {string|import("../featureloader.js").FeatureUrlFunction|undefined} The url.
   * @api
   */
  getUrl() {
    return this.url_;
  }

  /**
   * @param {Event} event Event.
   * @private
   */
  handleFeatureChange_(event) {
    const feature = /** @type {FeatureType} */ (event.target);
    const featureKey = getUid(feature);
    const geometry = feature.getGeometry();
    if (!geometry) {
      if (!(featureKey in this.nullGeometryFeatures_)) {
        if (this.featuresRtree_) {
          this.featuresRtree_.remove(feature);
        }
        this.nullGeometryFeatures_[featureKey] = feature;
      }
    } else {
      const extent = geometry.getExtent();
      if (featureKey in this.nullGeometryFeatures_) {
        delete this.nullGeometryFeatures_[featureKey];
        if (this.featuresRtree_) {
          this.featuresRtree_.insert(extent, feature);
        }
      } else {
        if (this.featuresRtree_) {
          this.featuresRtree_.update(extent, feature);
        }
      }
    }
    const id = feature.getId();
    if (id !== undefined) {
      const sid = id.toString();
      if (this.idIndex_[sid] !== feature) {
        this.removeFromIdIndex_(feature);
        this.idIndex_[sid] = feature;
      }
    } else {
      this.removeFromIdIndex_(feature);
      this.uidIndex_[featureKey] = feature;
    }
    this.changed();
    this.dispatchEvent(
      new VectorSourceEvent(VectorEventType.CHANGEFEATURE, feature),
    );
  }

  /**
   * Returns true if the feature is contained within the source.
   * @param {FeatureType} feature Feature.
   * @return {boolean} Has feature.
   * @api
   */
  hasFeature(feature) {
    const id = feature.getId();
    if (id !== undefined) {
      return id in this.idIndex_;
    }
    return getUid(feature) in this.uidIndex_;
  }

  /**
   * @return {boolean} Is empty.
   */
  isEmpty() {
    if (this.featuresRtree_) {
      return (
        this.featuresRtree_.isEmpty() && isEmpty(this.nullGeometryFeatures_)
      );
    }
    if (this.featuresCollection_) {
      return this.featuresCollection_.getLength() === 0;
    }
    return true;
  }

  /**
   * @param {import("../extent.js").Extent} extent Extent.
   * @param {number} resolution Resolution.
   * @param {import("../proj/Projection.js").default} projection Projection.
   */
  loadFeatures(extent, resolution, projection) {
    const loadedExtentsRtree = this.loadedExtentsRtree_;
    const extentsToLoad = this.strategy_(extent, resolution, projection);
    for (let i = 0, ii = extentsToLoad.length; i < ii; ++i) {
      const extentToLoad = extentsToLoad[i];
      const alreadyLoaded = loadedExtentsRtree.forEachInExtent(
        extentToLoad,
        /**
         * @param {{extent: import("../extent.js").Extent}} object Object.
         * @return {boolean} Contains.
         */
        function (object) {
          return containsExtent(object.extent, extentToLoad);
        },
      );
      if (!alreadyLoaded) {
        ++this.loadingExtentsCount_;
        this.dispatchEvent(
          new VectorSourceEvent(VectorEventType.FEATURESLOADSTART),
        );
        this.loader_.call(
          this,
          extentToLoad,
          resolution,
          projection,
          /**
           * @param {Array<FeatureType>} features Loaded features
           */
          (features) => {
            --this.loadingExtentsCount_;
            this.dispatchEvent(
              new VectorSourceEvent(
                VectorEventType.FEATURESLOADEND,
                undefined,
                features,
              ),
            );
          },
          () => {
            --this.loadingExtentsCount_;
            this.dispatchEvent(
              new VectorSourceEvent(VectorEventType.FEATURESLOADERROR),
            );
          },
        );
        loadedExtentsRtree.insert(extentToLoad, {extent: extentToLoad.slice()});
      }
    }
    this.loading =
      this.loader_.length < 4 ? false : this.loadingExtentsCount_ > 0;
  }

  /**
   * @override
   */
  refresh() {
    this.clear(true);
    this.loadedExtentsRtree_.clear();
    super.refresh();
  }

  /**
   * Remove an extent from the list of loaded extents.
   * @param {import("../extent.js").Extent} extent Extent.
   * @api
   */
  removeLoadedExtent(extent) {
    const loadedExtentsRtree = this.loadedExtentsRtree_;
    const obj = loadedExtentsRtree.forEachInExtent(extent, function (object) {
      if (equals(object.extent, extent)) {
        return object;
      }
    });
    if (obj) {
      loadedExtentsRtree.remove(obj);
    }
  }

  /**
   * Batch remove features from the source.  If you want to remove all features
   * at once, use the {@link module:ol/source/Vector~VectorSource#clear #clear()} method
   * instead.
   * @param {Array<FeatureType>} features Features to remove.
   * @api
   */
  removeFeatures(features) {
    let removed = false;
    for (let i = 0, ii = features.length; i < ii; ++i) {
      removed = this.removeFeatureInternal(features[i]) || removed;
    }
    if (removed) {
      this.changed();
    }
  }

  /**
   * Remove a single feature from the source. If you want to batch remove
   * features, use the {@link module:ol/source/Vector~VectorSource#removeFeatures #removeFeatures()} method
   * instead.
   * @param {FeatureType} feature Feature to remove.
   * @api
   */
  removeFeature(feature) {
    if (!feature) {
      return;
    }
    const removed = this.removeFeatureInternal(feature);
    if (removed) {
      this.changed();
    }
  }

  /**
   * Remove feature without firing a `change` event.
   * @param {FeatureType} feature Feature.
   * @return {boolean} True if the feature was removed, false if it was not found.
   * @protected
   */
  removeFeatureInternal(feature) {
    const featureKey = getUid(feature);
    if (!(featureKey in this.uidIndex_)) {
      return false;
    }

    if (featureKey in this.nullGeometryFeatures_) {
      delete this.nullGeometryFeatures_[featureKey];
    } else {
      if (this.featuresRtree_) {
        this.featuresRtree_.remove(feature);
      }
    }

    const featureChangeKeys = this.featureChangeKeys_[featureKey];
    featureChangeKeys?.forEach(unlistenByKey);
    delete this.featureChangeKeys_[featureKey];

    const id = feature.getId();
    if (id !== undefined) {
      const idString = id.toString();
      const indexedFeature = this.idIndex_[idString];
      if (indexedFeature === feature) {
        delete this.idIndex_[idString];
      } else if (Array.isArray(indexedFeature)) {
        indexedFeature.splice(indexedFeature.indexOf(feature), 1);
        if (indexedFeature.length === 1) {
          this.idIndex_[idString] = indexedFeature[0];
        }
      }
    }
    delete this.uidIndex_[featureKey];
    if (this.hasListener(VectorEventType.REMOVEFEATURE)) {
      this.dispatchEvent(
        new VectorSourceEvent(VectorEventType.REMOVEFEATURE, feature),
      );
    }
    return true;
  }

  /**
   * Remove a feature from the id index.  Called internally when the feature id
   * may have changed.
   * @param {FeatureType} feature The feature.
   * @private
   */
  removeFromIdIndex_(feature) {
    for (const id in this.idIndex_) {
      if (this.idIndex_[id] === feature) {
        delete this.idIndex_[id];
        break;
      }
    }
  }

  /**
   * Set the new loader of the source. The next render cycle will use the
   * new loader.
   * @param {import("../featureloader.js").FeatureLoader<FeatureType>} loader The loader to set.
   * @api
   */
  setLoader(loader) {
    this.loader_ = loader;
  }

  /**
   * Points the source to a new url. The next render cycle will use the new url.
   * @param {string|import("../featureloader.js").FeatureUrlFunction} url Url.
   * @api
   */
  setUrl(url) {
    assert(this.format_, '`format` must be set when `url` is set');
    this.url_ = url;
    this.setLoader(xhr(url, this.format_));
  }

  /**
   * @param {boolean} overlaps The source can have overlapping geometries.
   */
  setOverlaps(overlaps) {
    this.overlaps_ = overlaps;
    this.changed();
  }
}

export default VectorSource;
