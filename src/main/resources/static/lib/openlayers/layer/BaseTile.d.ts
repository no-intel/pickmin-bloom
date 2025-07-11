export default BaseTileLayer;
/**
 * *
 */
export type BaseTileLayerOnSignature<Return> = import("../Observable").OnSignature<import("../Observable").EventTypes, import("../events/Event.js").default, Return> & import("../Observable").OnSignature<import("./Base").BaseLayerObjectEventTypes | import("./Layer.js").LayerEventType | "change:preload" | "change:useInterimTilesOnError", import("../Object").ObjectEvent, Return> & import("../Observable").OnSignature<import("../render/EventType").LayerRenderEventTypes, import("../render/Event").default, Return> & import("../Observable").CombinedOnSignature<import("../Observable").EventTypes | import("./Base").BaseLayerObjectEventTypes | import("./Layer.js").LayerEventType | "change:preload" | "change:useInterimTilesOnError" | import("../render/EventType").LayerRenderEventTypes, Return>;
export type Options<TileSourceType extends import("../source/Tile.js").default> = {
    /**
     * A CSS class name to set to the layer element.
     */
    className?: string | undefined;
    /**
     * Opacity (0, 1).
     */
    opacity?: number | undefined;
    /**
     * Visibility.
     */
    visible?: boolean | undefined;
    /**
     * The bounding extent for layer rendering.  The layer will not be
     * rendered outside of this extent.
     */
    extent?: import("../extent.js").Extent | undefined;
    /**
     * The z-index for layer rendering.  At rendering time, the layers
     * will be ordered, first by Z-index and then by position. When `undefined`, a `zIndex` of 0 is assumed
     * for layers that are added to the map's `layers` collection, or `Infinity` when the layer's `setMap()`
     * method was used.
     */
    zIndex?: number | undefined;
    /**
     * The minimum resolution (inclusive) at which this layer will be
     * visible.
     */
    minResolution?: number | undefined;
    /**
     * The maximum resolution (exclusive) below which this layer will
     * be visible.
     */
    maxResolution?: number | undefined;
    /**
     * The minimum view zoom level (exclusive) above which this layer will be
     * visible.
     */
    minZoom?: number | undefined;
    /**
     * The maximum view zoom level (inclusive) at which this layer will
     * be visible.
     */
    maxZoom?: number | undefined;
    /**
     * Preload. Load low-resolution tiles up to `preload` levels. `0`
     * means no preloading.
     */
    preload?: number | undefined;
    /**
     * Source for this layer.
     */
    source?: TileSourceType | undefined;
    /**
     * Sets the layer as overlay on a map. The map will not manage
     * this layer in its layers collection, and the layer will be rendered on top. This is useful for
     * temporary layers. The standard way to add a layer to a map and have it managed by the map is to
     * use {@link import ("../map.js").default#addLayer map.addLayer()}.
     */
    map?: import("../Map.js").default | undefined;
    /**
     * Background color for the layer. If not specified, no background
     * will be rendered.
     */
    background?: import("./Base").BackgroundColor | undefined;
    /**
     * Deprecated.  Use interim tiles on error.
     */
    useInterimTilesOnError?: boolean | undefined;
    /**
     * Arbitrary observable properties. Can be accessed with `#get()` and `#set()`.
     */
    properties?: {
        [x: string]: any;
    } | undefined;
    /**
     * The internal tile cache size.  This needs to be large enough to render
     * two zoom levels worth of tiles.
     */
    cacheSize?: number | undefined;
};
/***
 * @template Return
 * @typedef {import("../Observable").OnSignature<import("../Observable").EventTypes, import("../events/Event.js").default, Return> &
 *   import("../Observable").OnSignature<import("./Base").BaseLayerObjectEventTypes|
 *     import("./Layer.js").LayerEventType|'change:preload'|'change:useInterimTilesOnError', import("../Object").ObjectEvent, Return> &
 *   import("../Observable").OnSignature<import("../render/EventType").LayerRenderEventTypes, import("../render/Event").default, Return> &
 *   import("../Observable").CombinedOnSignature<import("../Observable").EventTypes|import("./Base").BaseLayerObjectEventTypes|
 *   import("./Layer.js").LayerEventType|'change:preload'|'change:useInterimTilesOnError'|import("../render/EventType").LayerRenderEventTypes, Return>} BaseTileLayerOnSignature
 */
/**
 * @template {import("../source/Tile.js").default} TileSourceType
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
 * @property {number} [preload=0] Preload. Load low-resolution tiles up to `preload` levels. `0`
 * means no preloading.
 * @property {TileSourceType} [source] Source for this layer.
 * @property {import("../Map.js").default} [map] Sets the layer as overlay on a map. The map will not manage
 * this layer in its layers collection, and the layer will be rendered on top. This is useful for
 * temporary layers. The standard way to add a layer to a map and have it managed by the map is to
 * use {@link import("../map.js").default#addLayer map.addLayer()}.
 * @property {import("./Base.js").BackgroundColor} [background] Background color for the layer. If not specified, no background
 * will be rendered.
 * @property {boolean} [useInterimTilesOnError=true] Deprecated.  Use interim tiles on error.
 * @property {Object<string, *>} [properties] Arbitrary observable properties. Can be accessed with `#get()` and `#set()`.
 * @property {number} [cacheSize=512] The internal tile cache size.  This needs to be large enough to render
 * two zoom levels worth of tiles.
 */
/**
 * @classdesc
 * For layer sources that provide pre-rendered, tiled images in grids that are
 * organized by zoom levels for specific resolutions.
 * Note that any property set in the options is set as a {@link module:ol/Object~BaseObject}
 * property on the layer object; for example, setting `title: 'My Title'` in the
 * options means that `title` is observable, and has get/set accessors.
 *
 * @template {import("../source/Tile.js").default} TileSourceType
 * @template {import("../renderer/Layer.js").default} RendererType
 * @extends {Layer<TileSourceType, RendererType>}
 * @api
 */
declare class BaseTileLayer<TileSourceType extends import("../source/Tile.js").default, RendererType extends import("../renderer/Layer.js").default<any>> extends Layer<TileSourceType, RendererType> {
    /**
     * @param {Options<TileSourceType>} [options] Tile layer options.
     */
    constructor(options?: Options<TileSourceType>);
    /***
     * @type {BaseTileLayerOnSignature<import("../events").EventsKey>}
     */
    on: BaseTileLayerOnSignature<import("../events").EventsKey>;
    /***
     * @type {BaseTileLayerOnSignature<import("../events").EventsKey>}
     */
    once: BaseTileLayerOnSignature<import("../events").EventsKey>;
    /***
     * @type {BaseTileLayerOnSignature<void>}
     */
    un: BaseTileLayerOnSignature<void>;
    /**
     * @type {number|undefined}
     * @private
     */
    private cacheSize_;
    /**
     * @return {number|undefined} The suggested cache size
     * @protected
     */
    protected getCacheSize(): number | undefined;
    /**
     * Return the level as number to which we will preload tiles up to.
     * @return {number} The level to preload tiles up to.
     * @observable
     * @api
     */
    getPreload(): number;
    /**
     * Set the level as number to which we will preload tiles up to.
     * @param {number} preload The level to preload tiles up to.
     * @observable
     * @api
     */
    setPreload(preload: number): void;
    /**
     * Deprecated.  Whether we use interim tiles on error.
     * @return {boolean} Use interim tiles on error.
     * @observable
     * @api
     */
    getUseInterimTilesOnError(): boolean;
    /**
     * Deprecated.  Set whether we use interim tiles on error.
     * @param {boolean} useInterimTilesOnError Use interim tiles on error.
     * @observable
     * @api
     */
    setUseInterimTilesOnError(useInterimTilesOnError: boolean): void;
}
import Layer from './Layer.js';
//# sourceMappingURL=BaseTile.d.ts.map