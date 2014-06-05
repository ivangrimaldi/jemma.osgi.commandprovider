package it.ismb.jemma.deviceapi.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.energy_home.jemma.ah.hac.ApplianceException;
import org.energy_home.jemma.ah.hac.IAppliance;
import org.energy_home.jemma.ah.hac.IEndPoint;
import org.energy_home.jemma.ah.hac.IServiceCluster;
import org.energy_home.jemma.ah.hac.ServiceClusterException;
import org.energy_home.jemma.ah.hac.lib.ext.IAppliancesProxy;
import org.energy_home.jemma.ah.hac.lib.ext.TextConverter;
import org.osgi.service.component.ComponentContext;

public class ApiCommandProvider implements CommandProvider{

	private ComponentContext context;
	private IAppliancesProxy appliancesProxy;

	protected void activate(ComponentContext context)
	{

	}
	
	public void bindIAppliancesProxy(IAppliancesProxy appliancesProxy)
	{
		this.appliancesProxy=appliancesProxy;
	}

	public void unbindIAppliancesProxy(IAppliancesProxy appliancesProxy)
	{
		this.appliancesProxy=null;
	}

	@Override
	public String getHelp() {
		String help = "---Device APIs utils---\n";
		help += "\tlistAppliances - list appliances with endpoints and methods\n";
		help += "\tinvokeClusterMethod <appliancePid> <endPointId> <clusterName> <methodName> - invokes a cluster method and print the result in console\n";

		return help;
	}

	public void _invokeClusterMethod(CommandInterpreter ci) throws Exception
	{
		String appliancePid=ci.nextArgument();
		Integer endPointId=Integer.valueOf(ci.nextArgument());
		String clusterName=ci.nextArgument();
		String methodName=ci.nextArgument();
		
		Object[] objectParams = TextConverter.getObjectParameters(Class.forName( clusterName)
				, methodName,
				new String[0],//empty argument array, for testing
				appliancesProxy.getRequestContext(true));
		Object result = appliancesProxy.invokeClusterMethod(appliancePid, endPointId, clusterName, methodName, objectParams);
		
		ci.println(TextConverter.getTextRepresentation(result));
	}

	public void _listAppliances(CommandInterpreter ci) throws ApplianceException, ServiceClusterException
	{
		
		List<IAppliance> appliances=appliancesProxy.getAppliances();
		for(IAppliance appliance:appliances)
		{
			ci.println("######################");
			ci.println("Appliance: [pid:"+appliance.getPid()+
					" deviceType:"+appliance.getDescriptor().getDeviceType()+
					" friendlyName:"+appliance.getDescriptor().getFriendlyName()+
					" type:"+appliance.getDescriptor().getType()+
					"  ]");

			ci.println("Appliance endpoints:");
			IEndPoint[] eps= appliance.getEndPoints();

			for(int e=0;e<eps.length;e++)
			{
				IEndPoint ep=eps[e];
				ci.println("\t- EndPoint "+ep.getId());
				IServiceCluster[] clusters= ep.getServiceClusters();
				ci.println("\t  Clusters:");
				for(int c=0;c<clusters.length;c++)
				{
					IServiceCluster cluster=clusters[c];
					String side=cluster.getSide()==cluster.SERVER_SIDE?"server":"client";
					ci.println("\t\t Cluster:"+cluster.getName()+" side: "+side);
					ci.println("\t\t Methods:");
					Method[] methods;
					try {
						methods = Class.forName(cluster.getName()).getMethods();

						for(Method m:methods)
						{
							ci.println("\t\t\t "+m.getName());
						}
						
					} catch (SecurityException | ClassNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
			ci.println("######################");

		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
