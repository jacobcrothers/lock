import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-panels',
  templateUrl: './panels.component.html',
  styleUrls: ['./panels.component.css']
})
export class PanelsComponent implements OnInit {

  constructor() { }

  ngOnInit() {
    // TO DO get peENALS FROM be
  }

  mouseEnter(event) {
    console.log("hover on panel area", event);
  }

}
