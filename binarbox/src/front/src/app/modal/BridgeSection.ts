export class BridgeSection {
    private readonly _id: number;
    private readonly x1: number;
    private readonly y1: number;
    private readonly x2: number;
    private readonly y2: number;


    constructor(id: number, x1: number, y1: number, x2: number, y2: number) {
        this._id = id;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }


    get id(): number {
        return this._id;
    }


    getHtmlAreaCoordonates() {
        return [this.x1, this.y1, this.x2, this.y2].join(",");
    }


    getCurrentHeight(): number {
        return this.y2 - this.y1;
    }

    getCurrentWidth(): number {
        return this.x2 - this.x1;
    }

    /**
     * Calculate the next section position based on the current position
     * @param id BridgeSection unique id, used for identification
     * @param offsetRight number of pixels from where the next section will start
     * @param offsetBottom number of pixels from where the next section will start
     * @param width (Optional) overwritten width, in case this is not defined with take the current BridgeSection width
     * @param height (Optional) overwritten height, in case this is not defined with take the current BridgeSection height
     * @returns {BridgeSection}
     * */
    getNextSection(id: number, offsetRight: number, offsetBottom: number, width?: number, height?: number) {
        const sectionHeight = height || this.getCurrentHeight();
        const sectionWidth = width || this.getCurrentWidth();


        const next_x1 = this.x2 + offsetRight;
        const next_y1 = this.y1 + offsetBottom;
        const next_x2 = next_x1 + sectionWidth;
        const next_y2 = next_y1 + sectionHeight;

        return new BridgeSection(id, next_x1, next_y1, next_x2, next_y2);
    }


    /**
     * Returns the object used in template
     * @returns {Object}
     * */
    getFlattenObject() {

        return {
            id: this.id,
            coords: this.getHtmlAreaCoordonates()
        };
    }

}
