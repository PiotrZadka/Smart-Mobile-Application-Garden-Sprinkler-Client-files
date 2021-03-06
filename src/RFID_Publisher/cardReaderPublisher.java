package RFID_Publisher;
import org.eclipse.paho.client.mqttv3.*;

public class cardReaderPublisher {
	
	// MQTT brokers
    // public static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
     public static final String BROKER_URL = "tcp://iot.eclipse.org:1883";
    //public static final String BROKER_URL = "tcp://broker.mqttdashboard.com:1883";

    public static final String userid = "14056838"; // change this to be your student-id

    public static final String TOPIC_TEMPERATURE = userid +"/doorState";

    private MqttClient client;


    public cardReaderPublisher() {


        try {

            client = new MqttClient(BROKER_URL, userid);

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Publishing all data recieved from "cardReaderController"
    void start(String cardReaderDataJson) {
        try {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(false);
            options.setMaxInflight(1000);
            options.setAutomaticReconnect(true);
            options.setWill(client.getTopic(userid+"/LWT"), "I'm gone :(".getBytes(), 0, false);

            client.connect(options);
            
            //Publish data once
            publishReader(cardReaderDataJson);
            //Disconnect client in order to wait for another door state
            client.disconnect();

        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void publishReader(String cardReaderDataJson) throws MqttException {
        final MqttTopic temperatureTopic = client.getTopic(TOPIC_TEMPERATURE);
        
        temperatureTopic.publish(new MqttMessage(cardReaderDataJson.getBytes()));
        System.out.println("Publishing Data");
        System.out.println("Published data. Topic: " + temperatureTopic.getName() + "  Message: " + cardReaderDataJson);
    }
}
