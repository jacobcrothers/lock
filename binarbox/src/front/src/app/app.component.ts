import {Component, OnInit} from '@angular/core';
import {NavigationStart, Router} from '@angular/router';
import {filter} from 'rxjs/operators';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

    public showImgBg: boolean;

    constructor(
        private router: Router,
    ) { }

    ngOnInit() {
        this.router.events.pipe(
            filter(event => event instanceof NavigationStart)
        ).subscribe((navigationStart: NavigationStart) => {
            const isBaseURL = navigationStart.url === '/';
            this.showImgBg = !isBaseURL;
        });
    }
}
