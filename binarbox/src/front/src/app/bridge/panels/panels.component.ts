import { Component, OnInit, HostListener } from '@angular/core';
import { AddLockService } from '../../_services/add-lock.service';
import { ActivatedRoute, Router } from '@angular/router';


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
  public currentImage = '../../../assets/images/bridge/pod0.jpg';
  private panelSection: number;

  public displayFirstImg = true;
  public displaySecondImg = false;
  public displayThirdImg = false;
  public displayFourthImg = false;
  public bridgeImgs = [
    {
      id: 0,
      src: '../../../assets/images/bridge/pod0.jpg',
      display: true
    },
    {
      id: 1,
      src: '../../../assets/images/bridge/pod1.jpg',
      display: false
    },
    {
      id: 2,
      src: '../../../assets/images/bridge/pod2.jpg',
      display: false
    },
    {
      id: 3,
      src: '../../../assets/images/bridge/pod3.jpg',
      display: false
    }
  ];

  constructor(
    private addLockService: AddLockService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.createdLock = this.addLockService.createdLock;
    this.imageWidth = 6.25 * window.innerHeight;
  }

  @HostListener('window:resize', ['$event'])
  onResize(event?) {
    this.screenHeight = event.target.innerHeight;
    //image ratio times viewport height -> so that the image won't be stretched
    this.imageWidth = 6.25 * this.screenHeight;
  }

  onMousewheel(event) {
    if (event.deltaY < 0) {
      if (this.zoomCount < 3) {
        this.zoomCount = this.zoomCount + 1;
      } else {
        this.zoomCount = 3;
      }
    } else {
      if (this.zoomCount <= 3 && this.zoomCount > 0) {
        this.zoomCount = this.zoomCount - 1;
      } else {
        this.zoomCount = 0;
      }
    }
    // console.log('zoom count', this.zoomCount);
    this.currentImage = this.findBridgeImg(this.zoomCount);
    // this.displayCurrentImage(this.zoomCount);

    if (this.zoomCount === 3) {
      this.mouseEnter();
      $("map[name=image-map]").imageMapResize();
    }
  }

  // public isImageVisible(id) {
  //   console.log('id and zoom--', id, this.zoomCount);
  //   return id === this.zoomCount ? false : true;

  // }

  // public displayCurrentImage(count) {
  //   this.bridgeImgs.forEach(img => {
  //     if (img.id === count) {
  //       img.display = true;
  //     }
  //     img.display = false;
  //   });
  //   console.log('images---', this.bridgeImgs);
  // }

  findBridgeImg(count) {
    let newImg = this.bridgeImgs.find((img) => {
      return img.id === count;
    });
    return newImg.src;
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

  verifyImageSrc() {
    return this.currentImage.includes('pod3') ? true : false;
  }
  
}
