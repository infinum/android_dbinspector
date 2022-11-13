import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-delete-database',
  templateUrl: './delete-database.component.html',
  styleUrls: ['./delete-database.component.css']
})
export class DeleteDatabaseComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { name: string },
    public dialogRef: MatDialogRef<DeleteDatabaseComponent>
  ) {
  }

  onCancel(): void {
    this.dialogRef.close(false)
  }

  onConfirm(): void {
    this.dialogRef.close(true)
  }
}
