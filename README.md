jemma.osgi.commandprovider
==========================

A simple command provider for Jemma APIs

##Commands

	listAppliances - list appliances with endpoints and methods
	invokeClusterMethod <appliancePid> <endPointId> <clusterName> <methodName> - invokes a cluster method and print the result in console

##Example

###Get appliances list

	listAppliances

####Toggle on/off

	invokeClusterMethod ah.app.3521399293210526034 1 org.energy_home.jemma.ah.cluster.zigbee.general.OnOffServer execToggle

####Get Instantaneous Demand

	invokeClusterMethod ah.app.3521399293210526034 1 org.energy_home.jemma.ah.cluster.zigbee.metering.SimpleMeteringServer getIstantaneousDemand

####Get Divisor

	invokeClusterMethod ah.app.3521399293210526034 1 org.energy_home.jemma.ah.cluster.zigbee.metering.SimpleMeteringServer getDivisor

####Get Multiplier

	invokeClusterMethod ah.app.3521399293210526034 1 org.energy_home.jemma.ah.cluster.zigbee.metering.SimpleMeteringServer getDivisor

####GetUnitOfMeasure

	invokeClusterMethod ah.app.3521399293210526034 1 org.energy_home.jemma.ah.cluster.zigbee.metering.SimpleMeteringServer getUnitOfMeasure
	
0 means KW or KWh

Real instantaneous consumption value: Multiplier x Instantaneous Demand / Divisor \[UnitOfMeasure\]

