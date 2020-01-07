import { Component, OnInit, HostListener } from '@angular/core';
import { AddLockService } from '../../_services/add-lock.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BridgeSection } from "../../modal/BridgeSection";


@Component({
  selector: 'app-panels',
  templateUrl: './panels.component.html',
  styleUrls: ['./panels.component.scss']
})

@HostListener('mousewheel', ['$event'])

export class PanelsComponent implements OnInit {

  public createdLock: any;
  public zoomCount: number = 0;
  public screenHeight: any;
  public imageWidth: number;
  public panelSections: Array<Object>;
  public selectedSection: number;


  public displayFirstImg = true;
  public displaySecondImg = false;
  public displayThirdImg = false;
  public displayFourthImg = false;

  constructor(
    private addLockService: AddLockService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.createdLock = this.addLockService.createdLock;
    this.imageWidth = 6.25 * window.innerHeight;

     this.panelSections = this.generateSections(1,{
         x1: 110,
         y1: 540,
         x2: 578,
         y2: 724,
         height: 189,
         width: 474,
         offsetRight: 29,
         rows: 2
     });

  }

  /**
   * @param firstSectionId first section id
   * @param options
   * @param idGeneratorRule pattern to generate ids, if not specified the id will be incremented by one
   * */
    generateSections(firstSectionId: number, options: any, idGeneratorRule?: Function): Array<Object> {
        const sections: Array<Object> = new Array<Object>();
        const { x1, y1, x2, y2, width, height, offsetRight } = options;
        let element = new BridgeSection(1,x1, y1,x2,y2);

        sections.push(element.getFlattenObject());
        for(let i = 2; i<= 16; i++) {
            element = element.getNextSection(i,  i % 2 !== 0 ? offsetRight : 0, 0, width);
            sections.push(element.getFlattenObject());
        }

        element = new BridgeSection(17,x1, y1 + height, x2, y2 + height);
        sections.push(element.getFlattenObject());
        for(let i = 18; i<= 32; i++) {
            element = element.getNextSection(i,  i % 2 !== 0 ? offsetRight : 0, 0, width);
            sections.push(element.getFlattenObject());
        }

        return sections;
}


  @HostListener('window:resize', ['$event'])
  onResize(event?) {
    this.screenHeight = event.target.innerHeight;
    //image ratio times viewport height -> so that the image won't be stretched
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
  onMousewheel(event) {
    if (event.deltaY < 0) {
      if (this.zoomCount < 3) {
        this.zoomCount = this.zoomCount + 1;
      } else {
        this.zoomCount = 3;
      }
    }

    if (this.zoomCount === 3) {
      this.mouseEnter();
      $("map[name=image-map]").imageMapResize();
    }
  }

  chooseSection(event) {
    event.preventDefault();
    // send this to BE
    this.selectedSection = Number(event.target.id.split("-")[1]);
    this.addLockService.savePanelSection(this.selectedSection).subscribe(data => {
      this.router.navigate([`dashboard/payment`]);
    });
  }

  mouseEnter() {
    setTimeout(() => {
      $("map[name=image-map]").mapoid( {
        strokeColor: 'black',
        strokeWidth: 1,
        fillColor: 'black',
        fillOpacity: 0.5,
        fadeTime: 500,
        selectedArea: false,
        selectOnClick: true
      });
    }, 200)
  }
  
}
