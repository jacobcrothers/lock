import {NgModule} from '@angular/core';
import {
    IconCreditCard, 
    IconFacebook, 
    IconLock, 
    IconShare, 
    IconTwitter, 
    IconUser,
    IconChevronLeft,
    IconChevronRight,
    IconAlignJustify
} from 'angular-feather';

const icons = [
    IconFacebook,
    IconTwitter,
    IconUser,
    IconCreditCard,
    IconLock,
    IconShare,
    IconChevronLeft,
    IconChevronRight,
    IconAlignJustify
];

@NgModule({
    exports: icons
})

export class IconsModule {}
