import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-upload-button',
  templateUrl: './upload-button.component.html',
  styleUrls: ['./upload-button.component.sass']
})
export class UploadButtonComponent {

  @Input() file: any;
  @Input() multiple = false;
  @Input() callback: Function;
  @Output() fileChange = new EventEmitter<number>();

  onFileChange(fileInput: any) {
    if (fileInput.target.files && fileInput.target.files[0]) {
      if (this.multiple) {
        this.fileChange.emit(fileInput.target.files);
      } else {
        this.fileChange.emit(fileInput.target.files[0]);
      }
      this.callback();
    }
  }

}
