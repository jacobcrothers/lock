import { Component, OnInit } from '@angular/core';
import {UserService} from '../../_services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(
      private user: UserService
  ) { }

  ngOnInit() {
  }

  public login (formValue) {
      if (!formValue) {
          return;
      }

      this.user.login(formValue).subscribe(data => console.log(data));
  }

}
