import {AfterViewInit, Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import {AddLockService} from '../../_services/add-lock.service';
import {ActivatedRoute, Router} from '@angular/router';
import {BridgeSection} from '../../modal/BridgeSection';
import {fromEvent} from 'rxjs';
import {debounceTime, tap} from 'rxjs/operators';

interface SectionOptions {
    x1: number;
    y1: number;
    x2: number;
    y2: number;
    height: number;
    width: number;
    bridgePipeWidth: number;
}

@Component({
    selector: 'app-panels',
    templateUrl: './panels.component.html',
    styleUrls: ['./panels.component.scss']
})

export class PanelsComponent implements OnInit, AfterViewInit {

    @ViewChild('draggableContainer') draggableContainer: ElementRef;
    @ViewChild('panelContainer') panelContainer: ElementRef;

    public createdLock: any;
    public zoomCount = 0;
    public screenHeight: any;
    public imageWidth: number;
    public panelSections: ReturnType<BridgeSection['getFlattenObject']>[];
    public selectedSection: number;

    private position = {left: 0, x: 0};

    constructor(
        private addLockService: AddLockService,
        private route: ActivatedRoute,
        private router: Router
    ) {
    }

    ngOnInit() {
        if (this.addLockService.getLockId() === undefined) {
            this.router.navigate(['/locks/add-lock']);
        }

        this.createdLock = this.addLockService.createdLock;
        this.imageWidth = 6.25 * window.innerHeight;

        const firstSectionsRowHeight = 189;
        const bridgePipeWidth = 29;
        const firstRowOptions: SectionOptions = {
            x1: 100,
            y1: 549,
            x2: 581,
            y2: 741,
            height: firstSectionsRowHeight,
            width: 474,
            bridgePipeWidth,
        };
        const secondSectionsRowHeight = 242;
        const secondRowOptions = {
            x1: firstRowOptions.x1,
            y1: firstRowOptions.y1 + firstRowOptions.height,
            x2: firstRowOptions.x2,
            y2: firstRowOptions.y2 + secondSectionsRowHeight,
            height: secondSectionsRowHeight,
            width: firstRowOptions.width,
            bridgePipeWidth,
        };

        this.panelSections = [
            ...this.generateSectionsRange(firstRowOptions, [1, 16]),
            ...this.generateSectionsRange(secondRowOptions, [17, 32]),
        ];

        document.addEventListener('wheel', (e) => {
            this.onMousewheel(e);
        });
        this.onMousewheel1();
    }

    ngAfterViewInit() {
        fromEvent(this.panelContainer.nativeElement, 'click').pipe(
            debounceTime(4000),
            tap(this.onMousewheel)
        );

        fromEvent(this.panelContainer.nativeElement, 'touchmove').pipe(
            debounceTime(4000),
            tap(this.onMousewheel)
        );
    }
    //for testing automatic scrolling
    public onMousewheel1() {
        if (this.zoomCount < 3) {
            setTimeout(() => {
                this.zoomCount = 1;
                setTimeout(() => {
                    this.zoomCount = 2;
                    setTimeout(() => {
                        this.zoomCount = 3;
                        this.enableMapHighlight();
                        // TODO: refactor
                    }, 1000);
                }, 1000);
            }, 1000);
        }
    }
    /**
     * @param firstSectionId first section id
     * @param sectionOptions
     * @param idGeneratorRule pattern to generate ids, if not specified the id will be incremented by one
     * */
    private generateSectionsRange(sectionOptions: SectionOptions, [startSectionNr, endSectionNumber]: number[]) {
        const sections: ReturnType<BridgeSection['getFlattenObject']>[] = [];
        const {x1, y1, x2, y2, width, height, bridgePipeWidth} = sectionOptions;
        let element = new BridgeSection(startSectionNr, x1, y1, x2, y2);
        // 1 - 16
        sections.push(element.getFlattenObject());
        for (let i = startSectionNr + 1; i <= endSectionNumber; i++) {
            const currentSectionIsNearABridgePipe = i % 2 !== 0;
            element = element.getNextSection(i, currentSectionIsNearABridgePipe ? bridgePipeWidth : 0, 0, width);
            sections.push(element.getFlattenObject());
        }

        return sections;
    }

    @HostListener('window:resize', ['$event'])
    onResize(event?) {
        this.screenHeight = event.target.innerHeight;
        // image ratio times viewport height -> so that the image won't be stretched
        this.imageWidth = 6.25 * this.screenHeight;
    }

    /**
     * Handle scroll
     *
     * TODO update the implementation to lock scroll after the last image
     * - make it smooth and after the load
     * - add hint for the user to scroll to the desired section of the bridge
     *
     * */
    public onMousewheel(event) {
        if (this.zoomCount < 3) {
            setTimeout(() => {
                this.zoomCount = 1;
                setTimeout(() => {
                    this.zoomCount = 2;
                    setTimeout(() => {
                        this.zoomCount = 3;
                        this.enableMapHighlight();
                        // TODO: refactor
                    }, 1000);
                }, 1000);
            }, 1000);
        }
    }

    public chooseSection(event) {
        event.preventDefault();
        // send this to BE
        this.selectedSection = Number(event.target.id.split('-')[1]);
        this.addLockService.savePanelSection(this.selectedSection).subscribe(data => {
            this.router.navigate([`dashboard/payment`]);
        });
    }

    private enableMapHighlight() {
        // TODO: refactor

        ($('map[name=image-map]') as any).mapoid({
            strokeColor: 'black',
            strokeWidth: 1,
            fillColor: 'black',
            fillOpacity: 0.5,
            fadeTime: 500,
            selectedArea: false,
            selectOnClick: true
        });

        ($('map[name=image-map]') as any).imageMapResize();
    }

    /**
     * Handle drag the last image by using the mouse
     * */
    public onMouseDownHandler(event: MouseEvent) {
        // Change the cursor and prevent user from selecting the text
        this.draggableContainer.nativeElement.style.cursor = 'grabbing';
        this.draggableContainer.nativeElement.style.userSelect = 'none';

        this.position = {
            // The current scroll
            left: this.draggableContainer.nativeElement.scrollLeft,
            // Get the current mouse position
            x: event.clientX,
        };

        document.addEventListener('mousemove', this.mouseMoveHandler);
        document.addEventListener('mouseup', this.mouseUpHandler);
    }

    /**
     * How far the mouse has been moved we scroll the element
     * */
    private mouseMoveHandler = (event?) => {
        const dx = event.clientX - this.position.x;
        this.draggableContainer.nativeElement.scrollLeft = this.position.left - dx;
    };

    /**
     * When mouse up we remove the mousemove and mouseup events
     * */
    private mouseUpHandler = (event?) => {
        this.draggableContainer.nativeElement.style.cursor = 'grab';
        this.draggableContainer.nativeElement.style.removeProperty('user-select');

        document.removeEventListener('mousemove', this.mouseMoveHandler);
        document.removeEventListener('mouseup', this.mouseUpHandler);
    };
}
