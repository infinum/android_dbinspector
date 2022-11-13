import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

@Component({
  selector: 'app-drop-trigger',
  templateUrl: './drop-trigger.component.html',
  styleUrls: ['./drop-trigger.component.css']
})
export class DropTriggerComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { name: string },
    public dialogRef: MatDialogRef<DropTriggerComponent>
  ) {
  }

  onCancel(): void {
    this.dialogRef.close(false)
  }

  onConfirm(): void {
    this.dialogRef.close(true)
  }
}
