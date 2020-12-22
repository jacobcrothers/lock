import {AfterViewInit, Component, ElementRef, OnInit, Renderer2, ViewChild} from '@angular/core';

@Component({
    selector: 'app-cookies-policy',
    templateUrl: './cookies-policy.component.html',
    styleUrls: ['../_common/privacy.scss', './cookies-policy.component.scss']
})
export class CookiesPolicyComponent implements OnInit, AfterViewInit {
    @ViewChild('cookieDeclaration', {static: false}) cookieDeclaration: ElementRef;

    constructor(
        private renderer: Renderer2
    ) { }

    ngOnInit() {
    }

    ngAfterViewInit(): void {
        this.insertCookieDeclarationScript();
    }

    private insertCookieDeclarationScript() {
        const script = this.renderer.createElement('script');
        script.type = 'text/javascript';
        script.id = 'CookieDeclaration';
        script.src = 'https://consent.cookiebot.com/72662dcc-7ed6-4bad-bd29-5861675574af/cd.js';

        this.renderer.appendChild(this.cookieDeclaration.nativeElement, script);
    }
}
