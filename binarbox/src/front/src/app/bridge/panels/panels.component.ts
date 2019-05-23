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

    // const options = {
    //     top: 736,
    //     left: 110,
    //     width: 474,
    //     height: 189,
    //     offsetRight: 30
    // };

      const options = {
          top: 110,
          left: 736,
          width: 189,
          height: 474,
          offsetRight: 30
      };
     this.panelSection = this.generateSections(1,options);
     console.log(this.panelSection);

  }

  /**
   * @param firstSectionId first section id
   * @param options
   * @param idGeneratorRule pattern to generate ids, if not specified the id will be incremented by one
   * */
    generateSections(firstSectionId: number, options: any, idGeneratorRule?: Function): Array<Object> {
        const sections: Array<Object> = new Array<Object>();

        const { top, left, width, height } = options;
        let element = new BridgeSection(1,top,left,left + width, top + height);

        for(let i = 1; i<= 8; i++) {
            sections.push(element.getFlattenObject());
            element = element.getNextSection(i, i % 2 !== 0 ? 30 : 0, 0);
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
