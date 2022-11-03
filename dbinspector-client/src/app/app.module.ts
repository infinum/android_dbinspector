import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from "@angular/common/http";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatListModule} from "@angular/material/list";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {DatabasesComponent} from './databases/databases.component';
import {MatCardModule} from "@angular/material/card";
import {MatRippleModule} from "@angular/material/core";
import {DeleteDatabaseSheetComponent} from './delete-database-sheet/delete-database-sheet.component';
import {MatBottomSheetModule} from "@angular/material/bottom-sheet";

@NgModule({
  declarations: [
    AppComponent,
    DatabasesComponent,
    DeleteDatabaseSheetComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    MatToolbarModule,
    MatListModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatRippleModule,
    MatBottomSheetModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
