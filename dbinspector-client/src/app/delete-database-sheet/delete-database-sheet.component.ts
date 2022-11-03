import {Component, Inject, Input} from '@angular/core';
import {MatBottomSheetRef} from '@angular/material/bottom-sheet';
import {MAT_BOTTOM_SHEET_DATA} from '@angular/material/bottom-sheet';
import {DatabaseService} from "../database.service";

@Component({
  selector: 'app-delete-database-sheet',
  templateUrl: './delete-database-sheet.component.html',
  styleUrls: ['./delete-database-sheet.component.css']
})
export class DeleteDatabaseSheetComponent {

  private readonly databaseId?: string;
  databaseName?: string;

  constructor(
    @Inject(MAT_BOTTOM_SHEET_DATA) public data: { id: string, name: string },
    private _bottomSheetRef: MatBottomSheetRef<DeleteDatabaseSheetComponent>,
    private databaseService: DatabaseService
  ) {
    this.databaseId = data.id
    this.databaseName = data.name
  }

  deleteDatabase(): void {
    if (this.databaseId != null) {
      this.databaseService.deleteById(this.databaseId).subscribe()
    }
    this._bottomSheetRef.dismiss();
  }
}
