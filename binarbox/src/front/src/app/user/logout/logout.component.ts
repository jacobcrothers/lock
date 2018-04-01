import { Component, OnInit } from '@angular/core';
import {UserService} from '../../_services/user.service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  constructor() { }

  ngOnInit() {
      UserService.logout();
  }

}
