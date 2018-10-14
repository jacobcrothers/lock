import { Component, OnInit } from '@angular/core';
import { AddLockService } from '../../_services/add-lock.service';

@Component({
  selector: 'app-panels',
  templateUrl: './panels.component.html',
  styleUrls: ['./panels.component.css']
})
export class PanelsComponent implements OnInit {

  private createdLock: any;
  private smallImage = '../../../assets/images/pod0047.png';
  private largeImage='../../assets/images/pod0000.png'
  // private myImage = '../../../assets/images/background-image.jpeg';

  constructor(
    private addLockService: AddLockService
  ) { }

  ngOnInit() {
    // TO DO get peENALS FROM be
    this.createdLock = this.addLockService.createdLock;
    console.log('created lock from service--', this.createdLock);
  }

  mouseEnter(event) {
    console.log("hover on panel area", event);
  }

}
