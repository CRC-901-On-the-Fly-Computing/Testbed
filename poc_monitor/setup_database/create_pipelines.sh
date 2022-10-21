#!/bin/sh


execute() {
	# load urls
	. "./urls.sh"

	send PipelineAmpehre_csv2json
}


createPipeline() {
	curl "${elasticURL}/_ingest/pipeline/${_id}" \
		-f -s -S -o /dev/null \
		-X PUT \
		-H "Content-Type: application/json" \
		-d "${_pipeline}"
}

send() {
	echo "sending $1"

	# load data for index
	resetPipeline
	$1

	# send data to elasticsearch
	createPipeline
}



# pipelines

resetPipeline() {
	unset _id
	unset _pipeline
}

PipelineAmpehre_csv2json() {
	_id="ampehre_csv2json"

	_pipeline='
{
  "description": "Converts Ampehre CSV data into Json format",
  "processors": [
	{
	  "grok": {
	    "field": "_data",
	    "patterns": [ "%{INT:timestamp_s},%{INT:timestamp_ns},%{NUMBER:cpu0_power},%{NUMBER:cpu1_power},%{NUMBER:gpu_power},%{NUMBER:fpga_power},%{NUMBER:mic_power},%{NUMBER:sys_power},%{INT:cpu0_temp},%{INT:cpu1_temp},%{INT:gpu_temp},%{NUMBER:fpga_tempM},%{NUMBER:fpga_tempI},%{INT:mic_temp},%{NUMBER:sys_temp},%{NUMBER:cpu0_clock},%{NUMBER:cpu1_clock},%{INT:gpu_clock_graphics},%{INT:gpu_clock_mem},%{INT:mic_clock_core},%{INT:mic_clock_mem},%{NUMBER:cpu_util},%{INT:gpu_util_core},%{INT:gpu_util_mem},%{NUMBER:fpga_util},%{INT:mic_util},%{INT:cpu_memory},%{INT:cpu_swap},%{INT:gpu_memory},%{INT:mic_memory},%{INT:gpu_pcount}" ]
	  }
	},
	{
	  "set": {
	    "field": "_id",
	    "value": "{{timestamp_s}}"
	  }
	},
	{
	  "remove": {
	    "field": "_data"
	  }
	}
  ]
}
'
}



execute

