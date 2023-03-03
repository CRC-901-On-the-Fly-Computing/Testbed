# Angular Services

Here you can find all the services and their usage in the whole project.

* [D3](#d3)
* [dialog](#dialog)
* [executors](#executors)
* [loading](#loading)
* [messages](#messages)

## D3
With the help of D3, this service manages to create a visualization. At the moment we just have a force directed graph.

## dialog
It's a customized material dialog box.

### Import
```javascript
import { DialogService } from '../../../services/dialog/dialog.service';
```
### Instantiation
```javascript
constructor(
  private dialog: DialogService
) {}
```

### Usage
```javascript
this.dialog.open(json, 'json'); // show highlited JSON
this.dialog.open(json); // json is default type
this.dialog.open('sample test', 'text'); // show simple plain text
this.dialog.close(); // hide the dialog box
```

## executors
This service gets all executors data to parse them in a nice format.

### Import
```javascript
import { ExecutorsService } from '../../services/executors/executors.service';
```

### Instantiation
```javascript
constructor(
  private executorsService: ExecutorsService
) {}
```
### Usage

#### Get all executors and parse them
```javascript
this.executorsService.getAddresses(bUrl).subscribe(response => {

  // parse response and remove duplicates
  const ds = this.executorsService.parseUrls(response).filter(x => this.dataSource.findIndex(y => x.uuid === y.uuid) === -1);

  // merge lists
  this.dataSource = [...this.dataSource, ...ds];
  this.executorsService.getServices(bUrl).subscribe(_response => {
    this.compositions = { ...this.compositions, ...this.executorsService.parseCompositions(_response) };
  }, error => {
    console.log(error);
  });

}, error => {
  console.log(error);
});
```

#### Get logs for a specific executor
```javascript
this.executorsService.getLog(url, this.uuid).subscribe(response => {
  cosnole.log(response);
}, error => {
  cosnole.log(error);
});
```

## loading
Show a big loading spinner middle of the screen.

### Import
```javascript
import { LoadingService } from '../../services/loading/loading.service';
```

### Instantiation
```javascript
constructor(
  private loading: LoadingService
) {}
```

### Usage
```javascript
this.loading.show(); // show the loading in middle of the screen
this.loading.hide(); // hide the loading
```

## messages
Show a snack bar message at the bottom of the screen.

### Import
```javascript
import { MessageService } from '../../services/messages/messages.service';
```

### Instantiation
```javascript
constructor(
  private message: MessageService
) {}
```

### Usage
```javascript
this.message.success('You’re amazing!'); // show a success/green message
this.message.error('it’s your fault'); // show an error/red message
this.message.clear(); // hide the message
```