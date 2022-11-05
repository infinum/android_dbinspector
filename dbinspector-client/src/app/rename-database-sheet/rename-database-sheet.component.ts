import {Component, Inject} from '@angular/core';
import {MatBottomSheetRef} from '@angular/material/bottom-sheet';
import {MAT_BOTTOM_SHEET_DATA} from '@angular/material/bottom-sheet';
import {DatabaseService} from "../database.service";

@Component({
  selector: 'app-rename-database-sheet',
  templateUrl: './rename-database-sheet.component.html',
  styleUrls: ['./rename-database-sheet.component.css']
})
export class RenameDatabaseSheetComponent {

  private readonly databaseId?: string;
  databaseName?: string;

  newName?: string = this.databaseName

  constructor(
    @Inject(MAT_BOTTOM_SHEET_DATA) public data: { id: string, name: string },
    private _bottomSheetRef: MatBottomSheetRef<RenameDatabaseSheetComponent>,
    private databaseService: DatabaseService
  ) {
    this.databaseId = data.id
    this.databaseName = data.name
  }

  renameDatabase(): void {
    if (this.databaseId != null && this.newName != null && this.newName != "") {
      this.databaseService.renameById(this.databaseId, this.newName).subscribe()
    }
    this._bottomSheetRef.dismiss();
  }
}
