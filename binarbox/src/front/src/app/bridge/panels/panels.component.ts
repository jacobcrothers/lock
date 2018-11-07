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
  private stopScrolling: boolean = false;
  private zoomEl: any;

  constructor(
    private addLockService: AddLockService
  ) { }

  ngOnInit() {
    // TO DO get peENALS FROM be
    this.createdLock = this.addLockService.createdLock;
    console.log('created lock from service--', this.createdLock);
    this.addZoomFunctionality();
  }

  addZoomFunctionality() {

    // first solution
    this.zoomEl = mouseWheelZoom({
      element: document.querySelector('[data-wheel-zoom]'),
      zoomStep: .5
    });

    // second solution
    // this.zoomEl = mouseWheelZoom({
    //   element: document.querySelector('[data-wheel-zoom]'),
    //   zoomStep: 0
    // });
  }

  onMousewheel(event) {
    // first solution

    if (event.target.clientHeight > 4800) {
      this.stopScrolling = true;
      this.zoomEl.reset();
    }


    //second solution
    // let imgSrc = event.target.currentSrc;
    // let srcCount = imgSrc.substring(imgSrc.lastIndexOf('d') + 1, imgSrc.lastIndexOf('.'));

    // let newSrc;
    // if (event.deltaY < 0) {
    //  newSrc = '../../../assets/images/bridge/pod'.concat((parseInt(srcCount) + 1).toString(), '.png');
    // } else {
    //   newSrc = '../../../assets/images/bridge/pod'.concat((parseInt(srcCount) - 1).toString(), '.png');
    // }

    // this.zoomEl.setSrc(newSrc);
  }

  mouseEnter(event) {
    console.log("hover on panel area", event);
  }

}
