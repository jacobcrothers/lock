import { Component, OnInit, HostListener } from '@angular/core';
import { AddLockService } from '../../_services/add-lock.service';


@Component({
  selector: 'app-panels',
  templateUrl: './panels.component.html',
  styleUrls: ['./panels.component.css']
})

@HostListener('mousewheel', ['$event'])

export class PanelsComponent implements OnInit {

  private createdLock: any;
  private zoomCount: number = 0;
  private currentImage = '../../../assets/images/bridge/pod0.jpg';

  constructor(
    private addLockService: AddLockService
  ) { }

  ngOnInit() {
    this.createdLock = this.addLockService.createdLock;
  }

  onMousewheel(event) {
    // array from BE
    let bridgeImgs = [
      {
        id: 0,
        src: '../../../assets/images/bridge/pod0.jpg'
      },
      {
        id: 1,
        src: '../../../assets/images/bridge/pod1.jpg'
      },
      {
        id: 2,
        src: '../../../assets/images/bridge/pod2.jpg'
      },
      {
        id: 3,
        src: '../../../assets/images/bridge/pod3.png'
      }
    ]

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
    this.currentImage = this.findBridgeImg(bridgeImgs, this.zoomCount);

    if (this.zoomCount === 3) {
      this.mouseEnter();
      $("map[name=image-map]").imageMapResize();
    }
  }

  findBridgeImg(images, count) {
    let newImg = images.find((img) => {
      return img.id === count;
    });
    return newImg.src;
  }

  chooseSection(event) {
    event.preventDefault();
    // send this to BE
    console.log('section event--', event.target.id);
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
