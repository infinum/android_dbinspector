import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {HttpClientModule} from "@angular/common/http";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {DatabasesComponent} from './databases/databases.component';
import {MatCardModule} from "@angular/material/card";
import {DeleteDatabaseSheetComponent} from './delete-database-sheet/delete-database-sheet.component';
import {MatBottomSheetModule} from "@angular/material/bottom-sheet";
import {MatListModule} from "@angular/material/list";
import {RenameDatabaseSheetComponent} from './rename-database-sheet/rename-database-sheet.component';
import {MatLineModule, MatRippleModule} from "@angular/material/core";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormsModule} from "@angular/forms";
import {SchemaComponent} from './schema/schema.component';
import {MatTabsModule} from "@angular/material/tabs";
import {TableComponent} from './table/table.component';
import {MatTableModule} from "@angular/material/table";

@NgModule({
  declarations: [
    AppComponent,
    DatabasesComponent,
    DeleteDatabaseSheetComponent,
    RenameDatabaseSheetComponent,
    SchemaComponent,
    TableComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatBottomSheetModule,
    MatLineModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatTabsModule,
    MatListModule,
    MatRippleModule,
    MatTableModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
