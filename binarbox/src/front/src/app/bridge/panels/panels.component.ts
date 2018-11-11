import { Component, OnInit, HostListener } from '@angular/core';
import { AddLockService } from '../../_services/add-lock.service';
import { mouseWheelZoom } from 'mouse-wheel-zoom';


@Component({
  selector: 'app-panels',
  templateUrl: './panels.component.html',
  styleUrls: ['./panels.component.css']
})

@HostListener('mousewheel', ['$event'])

export class PanelsComponent implements OnInit {

  private createdLock: any;
  private zoomCount: number = 0;
  private currentImage = '../../../assets/images/bridge/pod0.png';

  constructor(
    private addLockService: AddLockService
  ) { }

  ngOnInit() {
    // TO DO get penals from BE
    this.createdLock = this.addLockService.createdLock;
  }

  onMousewheel(event) {
    // array from BE
    let bridgeImgs = [
      {
        id: 0,
        src: '../../../assets/images/bridge/pod0.png'
      },
      {
        id: 1,
        src: '../../../assets/images/bridge/pod1.png'
      },
      {
        id: 2,
        src: '../../../assets/images/bridge/pod2.png'
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
      console.log('image 3');
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
  }

  mouseEnter() {
    $("map[name=image-map]").mapoid( {
      strokeColor: 'red',
      strokeWidth: 1,
      fillColor: 'yellow',
      fillOpacity: 0.5,
      fadeTime: 500,
      selectedArea: false,
      selectOnClick: true
    });
  }

  mouseLeave() {
    // console.log('mouse leave---');
  }
}
