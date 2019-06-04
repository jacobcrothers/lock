import { Component, OnInit, HostListener } from '@angular/core';
import { AddLockService } from '../../_services/add-lock.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BridgeSection } from "../../modal/BridgeSection";


@Component({
  selector: 'app-panels',
  templateUrl: './panels.component.html',
  styleUrls: ['./panels.component.css']
})

@HostListener('mousewheel', ['$event'])

export class PanelsComponent implements OnInit {

  public createdLock: any;
  public zoomCount: number = 0;
  public screenHeight: any;
  public imageWidth: number;
  public panelSection: Array<Object>;

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

     this.panelSection = this.generateSections(1,{
         x1: 110,
         y1: 736,
         x2: 576,
         y2: 920,
         height: 189,
         width: 474,
         offsetRight: 28,
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

        for(let i = 1; i<= 16; i++) {
            sections.push(element.getFlattenObject());
            element = element.getNextSection(i,  i % 2 !== 0 ? offsetRight : 0, 0, width);
        }

        element = new BridgeSection(17,x1, y1 + height, x2, y2 + height);
        for(let i = 17; i<= 32; i++) {
              sections.push(element.getFlattenObject());
              element = element.getNextSection(i,  i % 2 !== 0 ? offsetRight : 0, 0, width);
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
    this.panelSection = event.target.id;
    this.addLockService.savePanelSection(this.panelSection).subscribe(data => {;
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
