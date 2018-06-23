import {NgModule} from '@angular/core';
import {IconCreditCard, IconFacebook, IconLock, IconShare, IconTwitter, IconUser} from 'angular-feather';

const icons = [
    IconFacebook,
    IconTwitter,
    IconUser,
    IconCreditCard,
    IconLock,
    IconShare
];

@NgModule({
    exports: icons
})

export class IconsModule {}
