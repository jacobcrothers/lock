export class BridgeSection {
    private readonly _id: number;
    private readonly _topPosition: number;
    private readonly _leftPosition: number;
    private readonly _rightPosition: number;
    private readonly _bottomPosition: number;


    constructor(id: number, topPosition: number, leftPosition: number, rightPosition: number, bottomPosition: number) {
        this._id = id;
        this._topPosition = topPosition;
        this._leftPosition = leftPosition;
        this._rightPosition = rightPosition;
        this._bottomPosition = bottomPosition;
    }


    get id(): number {
        return this._id;
    }

    get topPosition(): number {
        return this._topPosition;
    }

    get leftPosition(): number {
        return this._leftPosition;
    }

    get rightPosition(): number {
        return this._rightPosition;
    }

    get bottomPosition(): number {
        return this._bottomPosition;
    }


    getHtmlAreaCoordonates(): String {
        return [this._topPosition, this._leftPosition, this._bottomPosition, this._rightPosition].join(",");
    }


    getCurrentHight(): number {
        return this._bottomPosition - this._topPosition;
    }

    getCurrentWidth(): number {
        return this._rightPosition - this._leftPosition;
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
    getNextSection(id: number, offsetRight: number, offsetBottom: number, width?: number, height?: number, ) : BridgeSection {
        const sectionHeight = height || this.getCurrentHight();
        const sectionWidth = width || this.getCurrentWidth();


        const left = this.rightPosition + offsetRight;
        const right = left + sectionWidth;
        const top = this.topPosition + offsetBottom;
        const bottom = top + sectionHeight;

        return new BridgeSection(id, top,left,right,bottom);
    }


     /**
      * Returns the object used in template
      * @returns {Object}
      * */
    getFlattenObject(): Object {

        return {
            id: this.id,
            coords: this.getHtmlAreaCoordonates()
        }
    }

}
