# Elasticsearch and Kibana

This documentation was made for Elasticsearch and Kibana version 6.7.1.

Elasticsearch is mainly set up by Spring when the repositories are loaded. It
creates the indices with the mappings for the data with their properties and
data types. Only the ingest pipelines need to be managed manually.

The main part of Kibana can be im-/exported via the interface. This concretely
includes the dashboards, visualizations, searches and index patterns. Only a few
settings of Kibana have to be set manually.


## Setting up Elasticsearch and Kibana

To restore the ingest pipelines in Elasticsearch:

 1. set URLs for Elasticsearch and Kibana in `setup_database/urls.sh`
 2. run the script `setup_database/setup_database.sh`


To restore the dashboards, visualizations, searches and index patterns in Kibana:

 1. open Kibana
 2. navigate to: _Management -> Saved Objects_
 3. in the field `Please select a JSON file to import` click on `Import`
 4. open file `saved_objects_everything.json`
 5. click button `Import` on the bottom


Restore settings of Kibana:

 1. open Kibana
 2. navigate to: _Management -> Advanced Settings_
 3. set following settings:
     -  General -> Date format (`dateFormat`)  
        ```
        YYYY-MM-DD HH:mm:ss.SSS
        ```

     -  General -> Time picker defaults (`timepicker:timeDefaults`)  
        ```json
        {
          "from": "now-7d",
          "to": "now",
          "mode": "quick"
        }
        ```

     -  Search -> Ignore filter(s) (`courier:ignoreFilterIfFieldNotInIndex`)  
        `On`


## Backup Elasticsearch and Kibana

To backup the ingest pipelines from Elasticsearch:

 1. open Kibana
 2. navigate to: _Management -> Dev Tools_
 3. in console execute command `GET _ingest/pipeline/*` or `GET _ingest/pipeline/ID_OF_PIPELINE`
 4. copy the data
 5. save it in the file `setup_database/create_pipelines.sh` in appropriate function for ingest pipeline


To backup the dashboards, visualizations, searches and index patterns from Kibana:

 1. open Kibana
 2. navigate to: _Management -> Saved Objects_
 3. click button `Export %N% objects`
 4. select all types (dashboard, visualization, ...)
 5. click button `Export All`
 6. save to file `saved_objects_everything.json`
