#!/bin/bash
BRAINMATES_PATH="C:/Users/davide/Desktop/brainmates/Tablut/jars/BrAInmates.jar"
NEUROGENETIC_PATH="C:/Users/davide/Desktop/tablut/GeneticAlgorithmNeuromancer"
TIMEOUT=1

play() {
	echo "Removing game_start${1}"
	rm /tmp/game_start${1}
	rm logs/*
	java -jar jar/server.jar  &
	sleep 4
	java -jar jar/neurowhite.jar 1 ${TIMEOUT}  ${1} localhost > white${1}.log &
	java -jar ${BRAINMATES_PATH} black ${TIMEOUT} localhost > black${1}.log
	sleep 4
	#cat logs/*Neuromance* | wc -l | tr -d ' ' >> /tmp/NeuroClientOutput.txt${1} WE DO NOT USE IDEXES
	cat logs/*Neuromance* | wc -l | tr -d ' ' >> /tmp/NeuroClientOutput.txt
	echo "Creating genetic_start${1}"
	touch /tmp/genetic_start${1}
}

if (( $# != 1 )); then
    >&2 echo "Please specify process ID"
		exit
fi

ID=${1}

it=1
killall java # Dangerous :)
rm genetic${ID}.log black${ID}.log white${ID}.log server${ID}.log
play ${ID}
java -cp ${NEUROGENETIC_PATH} MainGeneticAlgorithm  0  WHITE  &
#while [[ $(grep WIN /tmp/NeuroClientOutput.txt${ID} | wc -l) -ne 1 ]] WE DO NOT USE INDEXES
while [[ $(grep WIN /tmp/NeuroClientOutput.txt | wc -l) -ne 1 ]]
do
	#grep WIN /tmp/NeuroClientOutput.txt${ID} | wc -l WE DO NOT USE INDEXES
	grep WIN /tmp/NeuroClientOutput.txt | wc -l
	echo "Waiting for /tmp/game_start${ID}"
	while [[  ! -f /tmp/game_start${ID} ]]  
	do
		sleep 1
	done
	echo "Iteration ${it}"
	play ${ID}
	it=$(expr ${it} + 1)
done

