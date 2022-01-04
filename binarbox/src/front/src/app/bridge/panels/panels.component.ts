import {AfterViewInit, Component, ElementRef, HostListener, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {AddLockService} from '../../_services/add-lock.service';
import {ActivatedRoute, Router} from '@angular/router';
import {BridgeSection} from '../../modal/BridgeSection';
import {fromEvent, Observable, Subscription} from 'rxjs';
import {debounceTime, finalize, map, switchMap, takeUntil, tap} from 'rxjs/operators';

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

export class PanelsComponent implements OnInit, AfterViewInit, OnDestroy {

    @ViewChild('draggableContainer') draggableContainer: ElementRef;
    @ViewChild('panelContainer') panelContainer: ElementRef;
    @ViewChild('draggableBridgeImage') draggableBridgeImage: ElementRef;

    public createdLock: any;
    public zoomCount = 0;
    public screenHeight: any;
    public imageWidth: number;
    public imageleft: any;
    public imagestick: any;
    public smallwidth: any;
    public panelSections: ReturnType<BridgeSection['getFlattenObject']>[];
    public panelSections1: any = [];
    public panelSections2: any = [];
    public panelSections3: any = [];
    public panelSections4: any = [];
    public panelSections5: any = [];
    public panelSections6: any = [];

    public panelSections7: any = [];
    public panelSections8: any = [];
    public selectedSection: number;

    private move$ = fromEvent<MouseEvent>(document, 'mousemove');
    private up$ = fromEvent<MouseEvent>(document, 'mouseup');
    private bridgeImageDrag$: Observable<void>;
    private bridgeImageDragSubscription: Subscription;

    constructor(
        private addLockService: AddLockService,
        private route: ActivatedRoute,
        private router: Router
    ) {
    }

    ngOnInit() {
        // image ratio times viewport height -> so that the image won't be stretched
        if (this.addLockService.getLockId() === undefined) {
            this.router.navigate(['/locks/add-lock']);
        }

        this.createdLock = this.addLockService.createdLock;
        this.imageWidth = 6.25 * window.innerHeight;

        this.imageleft = this.imageWidth * 0.01216;
        this.imageleft = this.imageleft.toString() + "px";

        this.imagestick = this.imageWidth * 0.006548 / 2;
        this.imagestick = this.imagestick.toString() + "px";

        this.smallwidth = this.imageWidth * 0.0625;

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

        for (var i = 1; i <= this.panelSections.length; i +=4) {
            let id = this.panelSections[i - 1].id;
            let coords = this.panelSections[i - 1].coords;
            this.panelSections1.push({key: i, id: id, coords: coords});
        }

        for (var i = 2; i <= this.panelSections.length; i +=4) {
            let id = this.panelSections[i - 1].id;
            let coords = this.panelSections[i - 1].coords;
            this.panelSections2.push({key: i, id: id, coords: coords});
        }

        for (var i = 3; i <= this.panelSections.length; i +=4) {
            let id = this.panelSections[i - 1].id;
            let coords = this.panelSections[i - 1].coords;
            this.panelSections3.push({key: i, id: id, coords: coords});
        }

        for (var i = 4; i <= this.panelSections.length; i +=4) {
            let id = this.panelSections[i - 1].id;
            let coords = this.panelSections[i - 1].coords;
            this.panelSections4.push({key: i, id: id, coords: coords});
        }

        this.panelSections5 = this.panelSections1.concat(this.panelSections2);
        this.panelSections6 = this.panelSections3.concat(this.panelSections4);

        this.panelSections5 = this.panelSections5.sort((a, b) => a.key - b.key);
        this.panelSections6 = this.panelSections6.sort((a, b) => a.key - b.key);
        document.addEventListener('wheel', (e) => {
            this.onMousewheel(e);
        });
         
    }
           /**
         Automatically scrolling the image after clicking "Add lock" button
         *
         * */
    public autoScroll() {
        
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


    ngAfterViewInit() {
        fromEvent(this.panelContainer.nativeElement, 'click').pipe(
            debounceTime(4000),
            tap(this.onMousewheel)
            
        );
        

        fromEvent(this.panelContainer.nativeElement, 'touchmove').pipe(
            debounceTime(4000),
            tap(this.onMousewheel)
            
        
        );

        const down$ = fromEvent<MouseEvent>(this.draggableBridgeImage.nativeElement, 'mousedown');
        this.bridgeImageDrag$ = down$.pipe(
            tap(() => {
                // Change the cursor and prevent user from selecting the text
                this.draggableContainer.nativeElement.style.cursor = 'grabbing';
                this.draggableContainer.nativeElement.style.userSelect = 'none';
            }),
            map((event) => {
                return {
                    // The current scroll
                    left: this.draggableContainer.nativeElement.scrollLeft,
                    // Get the current mouse position
                    x: event.clientX,
                };
            }),
            switchMap(position => this.move$.pipe(
                    map(move => {
                        const dx = move.clientX - position.x;
                        this.draggableContainer.nativeElement.scrollLeft = position.left - dx;
                    }),
                    takeUntil(this.up$),
                    finalize(() => {
                        this.draggableContainer.nativeElement.style.cursor = 'grab';
                        this.draggableContainer.nativeElement.style.removeProperty('user-select');
                    })
                )
            )
        );
        this.pageScroll();
    }

    ngAferViewChecked(): void {
        console.log("ngAfterViewChecked");
   
    }

    ngOnDestroy(): void {
        this.bridgeImageDragSubscription?.unsubscribe();
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
        this.imageleft = this.imageWidth * 0.01216;
        this.imageleft = this.imageleft.toString() + "px";

        this.imagestick = this.imageWidth * 0.006548 / 2;
        this.imagestick = this.imagestick.toString() + "px";

        this.smallwidth = this.imageWidth * 0.0625;
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

    public gotoPayment() {
        this.router.navigate([`dashboard/payment`]);
    }

    private enableMapHighlight() {
        // TODO: refactor
        this.bridgeImageDragSubscription = this.bridgeImageDrag$.subscribe();

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

    public pageScroll() {
        this.autoScroll();
    }
}
