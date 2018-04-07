import {NgModule} from '@angular/core';
import {IconCreditCard, IconFacebook, IconLock, IconUser} from 'angular-feather';

const icons = [
    IconFacebook,
    IconUser,
    IconCreditCard,
    IconLock
];

@NgModule({
    exports: icons
})

export class IconsModule {}
