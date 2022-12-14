import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  public isSidemenuActive: boolean;

  constructor() { }

  ngOnInit() {
    this.isSidemenuActive = true;
  }

  openNav() {
    this.isSidemenuActive = true;
  }

  closeNav() {
    this.isSidemenuActive = false;
  }

}
