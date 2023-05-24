import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { VarDirective } from './ngvar';

@NgModule({
    imports: [
        BrowserModule,
    ],
    declarations: [
        VarDirective
    ],
    exports: [
        VarDirective
    ],
    providers: [
        VarDirective
    ]
})
export class DirectiveModule { }
