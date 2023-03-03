# Angular Components

Here you can find all components and their usage in the whole project.

- [Angular Components](#angular-components)
	- [chatbox-message](#chatbox-message)
		- [message object](#message-object)
		- [usage](#usage)
			- [Single bubble message](#single-bubble-message)
			- [In chat bot](#in-chat-bot)
	- [dialog](#dialog)
	- [flag-icon](#flag-icon)
		- [Usage](#usage-1)
	- [loading](#loading)
	- [messages](#messages)
	- [rating-box](#rating-box)
		- [Usage](#usage-2)
	- [roles-dropdown](#roles-dropdown)
		- [usage](#usage-3)
	- [star](#star)
		- [Usage](#usage-4)
	- [upload-button](#upload-button)
		- [Usage](#usage-5)
	- [visuals](#visuals)
		- [Graph](#graph)
			- [Usage](#usage-6)

## chatbox-message
chatbox-message is a bubble message for requester page and it has these inputs:

| Input   | Type   | Description                    |
|---------|--------|--------------------------------|
| message | object | A message object               |
| URL     | string | URL of uploading training file |

### message object
It defines the type of message and its content.
Here are the inputs:

|     Input     | Type          | Description                                                                             |
|-------------|---------------|--------------------------------------------------------------------------------------|
| type          | string (enum) | Type of message. It can be `FILE`, `NO_QUESTION`, `DROPDOWN`, `NUMBER` or `TEXT`        |
| extra         | Array         | It's an array of any and it can be used as extra input, like dropdown data.                |
| body          | string        | The text of bubble message                                                              |
| timestamp     | Date          | Showing date                                                                            |
| from          | string (enum) | Define the sender of the message. It can be `bot` or `user`.                                |
| callback      | Function      | Callback function of each message. Defines what will exactly happen after. message    |
| extraCallback | Function      | Callback function for extra input. For example what happened if a user clicks on the dropdown.|
| questionID    | string        | Related to interview of PROSECO                                                         |

### usage
#### Single bubble message
```javascript
<app-message [message]="msg" [URL]="fileUploadURL"></app-message>
```
#### In chat bot
```javascript
  <div class="full-width" *ngFor="let msg of messages; let last = last">
    <app-message [message]="msg" [URL]="fileUploadURL"></app-message>
    {{ last ? scrollToBottom() : '' }}
  </div>
```
## dialog
It's a customized material dialog box. For usage please check the [dialog service](./services/#dialog).

## flag-icon
Shows the flag of input country.
Here are inputs:

| Input   | Type   | Description                                                                                   |
|---------|--------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| country | string | Name of the country. It should be in [data.json](https://git.cs.upb.de/SFB901-Testbed/Testbed/blob/feature/working_gui/website/WebContent_new/src/app/components/flag-icon/data.json) file. |

### Usage
Single usage
```javascript
<app-flag-icon country='germany'></app-flag-icon>
```
List of countries
```javascript
<mat-chip *ngFor="let c of offer.simplePolicy.allowedCountries">
  <app-flag-icon [country]='c'></app-flag-icon>
  {{c}}
</mat-chip>
```
## loading
Show a big loading spinner middle of screen. For usage please check the [loading service](./services/#loading).

## messages
Show a snack bar message at the bottom of the screen. For usage please check the [messages service](./services/#messages).

## rating-box
Showing rating box in detail. It used in the project for hover purposes.
Here are the inputs:

| Input  | Type          | Description                                          |
|--------|---------------|------------------------------------------------------|
| rating | object        | Rating object for backend API.                       |
| size   | string (enum) | Size of the box. It can be `small` or `large`.       |
| page   | string        | Name of the current page, in case of a custom CSS class. |

### Usage
```javascript
<app-rating-box [rating]="serviceRatings[item]">
  <app-star [count]="serviceRatings[item] !== undefined ? serviceRatings[item].overall : 0"></app-star>
</app-rating-box>
```
**P.S:** You can to pass the trigger tag inside the component. Here we used `app-star` because it show rating-box when user hover the rating stars.
## roles-dropdown
Showing the role chooser that is in top right corner.
### usage
```javascript
<app-roles-dropdown></app-roles-dropdown>
```
## star
Start component showing up to 5 stars for rating purposes.
Here are the inputs:

| Input  | Type          | Description                                       |
|--------|---------------|---------------------------------------------------|
| count  | number        | the current number of rate. It can be `0` to `5`. |
| size   | string (enum) | Size of the box. It can be `small` or `large`.    |
| change | boolean       | Define click on stars should work or not.         |
| style  | object        | Custom CSS style                                  |

### Usage
```javascript
<app-star *ngIf="item.rate" [count]="item.rate"></app-star>
```
## upload-button
An upload button, for upload files.
Here are the inputs:

| Input  | Type          | Description                                       |
|--------|---------------|---------------------------------------------------|
| file  | any        | File input. |
| multiple | boolean       | Accepts multiple files.         |
| callback   | function | Callback after selecting file.    |

### Usage
```javascript
<app-upload-button [(file)]="authenticationFile" [callback]="theBoundCallback">
  Upload
</app-upload-button>
```

## visuals
This components has visuals of `Graph`, `Nodes` and `Links`.

### Graph
Showing a forced directed graph with the help of D3.
Here are the inputs:

| Input | Type        | Description    |
|-------|-------------|----------------|
| nodes | Array<node> | Array of nodes |
| links | Array<link> | Array of links |
| scale | number      | Scale of chart |

#### Usage
```javascript
<app-graph *ngIf="this.nodes.length" [nodes]="nodes" [links]="links"></app-graph>
```
