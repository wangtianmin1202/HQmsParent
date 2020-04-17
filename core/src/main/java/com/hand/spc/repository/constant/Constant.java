package com.hand.spc.repository.constant;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

import com.hand.spc.utils.PropertiesUtils;

/**
 * Created by slj on 2019-07-05.
 */
public  class Constant {

    public static String mqttClientId="mqtt.clientId";
    public static String mqttQos="mqtt.qos";
    public static String mqttConnectTimeout="mqtt.connectTimeout";
    public static String mqttKeepaLiveInterval="mqtt.keepaLiveInterval";
    public static String  mqttRetained="mqtt.retained";
    public static String  mqttClearSession="mqtt.clearSession";
    public static String  kafkaAcks="kafka.acks";
    public static String  kafkaRetries="kafka.retries";
    public static String  kafkaBatchSize="kafka.batchSize";
    public static String  kafkaLingerMs="kafka.lingerMs";
    public static String  kafkaBufferMemory="kafka.bufferMemory";
    public static String  kafkakeySerializer="kafka.keySerializer";
    public static String  kafkaValueSerializer="kafka.valueSerializer";
    public static String  kafkaEnableAutoCommit="kafka.enableAutoCommit";
    public static String  kafkaAutoCommitIntervalms="kafka.autoCommitIntervalms";
    public static String  kafkaAutoOffsetReset="kafka.autoOffsetReset";
    public static String  kafkaSessionTimeOutMs="kafka.sessionTimeOutMs";
    public static String  kafkakeyDeserializer="kafka.keyDeserializer";
    public static String  kafkaValueDeserializer="kafka.valueDeserializer";
    public static String  kafkaGroupId="kafka.groupId";
    public static String  kafkaClientId="kafka.clientId";
    public static String  partitionerClass="partitioner.class";

    /**
     *   为了避免分布式系统导致的进程冲突，判断当前服务器必须和配置文件中配置的CLIEN一致才会运行
     * @return
     */

    public static ArrayList<String> getLocalIpAddr()
    {
        ArrayList<String> ipList = new ArrayList<String>();
        InetAddress[] addrList;
        try
        {
            Enumeration interfaces= NetworkInterface.getNetworkInterfaces();
            while(interfaces.hasMoreElements())
            {
                NetworkInterface ni=(NetworkInterface)interfaces.nextElement();
                Enumeration ipAddrEnum = ni.getInetAddresses();
                while(ipAddrEnum.hasMoreElements())
                {
                    InetAddress addr = (InetAddress)ipAddrEnum.nextElement();
                    if (addr.isLoopbackAddress() == true)
                    {
                        continue;
                    }

                    String ip = addr.getHostAddress();
                    if (ip.indexOf(":") != -1)
                    {
                        //skip the IPv6 addr
                        continue;
                    }
                    ipList.add(ip);
                }
            }
            Collections.sort(ipList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException("Failed to get local ip list");
        }

        return ipList;
    }




    public static boolean CheckIpAdress(){

            ArrayList<String> addrList = getLocalIpAddr();
            String clientId=PropertiesUtils.getConfig().getProperty(Constant.mqttClientId);
            String[]clientIds=clientId.split(";");
            for(String address:addrList) {
                for (int i = 0; i < clientIds.length; i++) {
                    if (address.toString().contains(clientIds[i])) {
                        return true;
                    }
                }
            }
        return false;
    }

}
