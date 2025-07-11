/**
 * @module ol/layer/VectorImage
 */
import BaseVectorLayer from './BaseVector.js';
import CanvasVectorImageLayerRenderer from '../renderer/canvas/VectorImageLayer.js';

/**
 * @template {import("../source/Vector.js").default<FeatureType>} [VectorSourceType=import("../source/vectorLayer.js").default<*>]
 * @template {import('../Feature.js').FeatureLike} [FeatureType=import("./BaseVector.js").ExtractedFeatureType<VectorSourceType>]
 * @typedef {Object} Options
 * @property {string} [className='ol-layer'] A CSS class name to set to the layer element.
 * @property {number} [opacity=1] Opacity (0, 1).
 * @property {boolean} [visible=true] Visibility.
 * @property {import("../extent.js").Extent} [extent] The bounding extent for layer rendering.  The layer will not be
 * rendered outside of this extent.
 * @property {number} [zIndex] The z-index for layer rendering.  At rendering time, the layers
 * will be ordered, first by Z-index and then by position. When `undefined`, a `zIndex` of 0 is assumed
 * for layers that are added to the map's `layers` collection, or `Infinity` when the layer's `setMap()`
 * method was used.
 * @property {number} [minResolution] The minimum resolution (inclusive) at which this layer will be
 * visible.
 * @property {number} [maxResolution] The maximum resolution (exclusive) below which this layer will
 * be visible.
 * @property {number} [minZoom] The minimum view zoom level (exclusive) above which this layer will be
 * visible.
 * @property {number} [maxZoom] The maximum view zoom level (inclusive) at which this layer will
 * be visible.
 * @property {import("../render.js").OrderFunction} [renderOrder] Render order. Function to be used when sorting
 * features before rendering. By default features are drawn in the order that they are created. Use
 * `null` to avoid the sort, but get an undefined draw order.
 * @property {number} [renderBuffer=100] The buffer in pixels around the viewport extent used by the
 * renderer when getting features from the vector source for the rendering or hit-detection.
 * Recommended value: the size of the largest symbol, line width or label.
 * @property {VectorSourceType} [source] Source.
 * @property {import("../Map.js").default} [map] Sets the layer as overlay on a map. The map will not manage
 * this layer in its layers collection, and the layer will be rendered on top. This is useful for
 * temporary layers. The standard way to add a layer to a map and have it managed by the map is to
 * use [map.addLayer()]{@link import("../Map.js").default#addLayer}.
 * @property {boolean|string|number} [declutter=false] Declutter images and text on this layer. Any truthy value will enable
 * decluttering. The priority is defined by the `zIndex` of the style and the render order of features. Higher z-index means higher
 * priority. Within the same z-index, a feature rendered before another has higher priority. Items will
 * not be decluttered against or together with items on other layers with the same `declutter` value. If
 * that is needed, use {@link import("../layer/Vector.js").default} instead.
 * @property {import("../style/Style.js").StyleLike|import("../style/flat.js").FlatStyleLike|null} [style] Layer style. When set to `null`, only
 * features that have their own style will be rendered. See {@link module:ol/style/Style~Style} for the default style
 * which will be used if this is not set.
 * @property {import("./Base.js").BackgroundColor} [background] Background color for the layer. If not specified, no background
 * will be rendered.
 * @property {number} [imageRatio=1] Ratio by which the rendered extent should be larger than the
 * viewport extent. A larger ratio avoids cut images during panning, but will cause a decrease in performance.
 * @property {Object<string, *>} [properties] Arbitrary observable properties. Can be accessed with `#get()` and `#set()`.
 */

/**
 * @classdesc
 * Vector data is rendered client-side, to an image. This layer type provides great performance
 * during panning and zooming, but point symbols and texts are always rotated with the view and
 * pixels are scaled during zoom animations. For more accurate rendering of vector data, use
 * {@link module:ol/layer/Vector~VectorLayer} instead.
 *
 * Note that any property set in the options is set as a {@link module:ol/Object~BaseObject}
 * property on the layer object; for example, setting `title: 'My Title'` in the
 * options means that `title` is observable, and has get/set accessors.
 *
 * @template {import("../source/Vector.js").default<FeatureType>} [VectorSourceType=import("../source/vectorLayer.js").default<*>]
 * @template {import('../Feature.js').FeatureLike} [FeatureType=import("./BaseVector.js").ExtractedFeatureType<VectorSourceType>]
 * @extends {BaseVectorLayer<FeatureType, VectorSourceType, CanvasVectorImageLayerRenderer>}
 * @api
 */
class VectorImageLayer extends BaseVectorLayer {
  /**
   * @param {Options<VectorSourceType, FeatureType>} [options] Options.
   */
  constructor(options) {
    options = options ? options : {};

    const baseOptions = Object.assign({}, options);
    delete baseOptions.imageRatio;
    super(baseOptions);

    /**
     * @type {number}
     * @private
     */
    this.imageRatio_ =
      options.imageRatio !== undefined ? options.imageRatio : 1;
  }

  /**
   * @return {number} Ratio between rendered extent size and viewport extent size.
   */
  getImageRatio() {
    return this.imageRatio_;
  }

  /**
   * @override
   */
  createRenderer() {
    return new CanvasVectorImageLayerRenderer(this);
  }
}

export default VectorImageLayer;
