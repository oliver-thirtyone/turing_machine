//package turingmachine.parser;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import javax.xml.XMLConstants;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.transform.Source;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamSource;
//import javax.xml.validation.SchemaFactory;
//import javax.xml.validation.Validator;
//
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import util.resources.ResourceManager;
//import eventmanager.entities.item.ItemManager;
//import eventmanager.entities.items.event.EuropayEvent;
//import eventmanager.entities.items.event.Event;
//import eventmanager.entities.items.event.FollowupEvent;
//import eventmanager.entities.items.event.Mapping;
//import eventmanager.entities.items.event.Text;
//import eventmanager.entities.items.event.enums.Language;
//import eventmanager.entities.items.event.enums.Led;
//import eventmanager.entities.items.event.enums.Output;
//import eventmanager.entities.items.event.enums.Relay;
//import eventmanager.entities.items.event.enums.Type;
//import eventmanager.entities.items.provider.Provider;
//import eventmanager.entities.items.range.Range;
//import eventmanager.model.parser.DataReader;
//
//public class XMLDataReader {
//
//	private DocumentBuilder documentBuilder;
//	private Validator validator;
//
//	public XMLDataReader() {
//		try {
//			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
//			this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
//
//			InputStream schema = new InputStream() {
//				@Override
//				public int read() throws IOException {
//					// TODO Auto-generated method stub
//					return 0;
//				}
//			}; // TODO: set schema
//			Source source = new StreamSource(schema);
//
//			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
//			this.validator = schemaFactory.newSchema(source).newValidator();
//		} catch (Exception exception) {
//			exception.printStackTrace();
//		}
//	}
//
//	public void readData(InputStream inputStream) throws SAXException, IOException {
//		Document document = this.documentBuilder.parse(inputStream);
//		document.getDocumentElement().normalize();
//
//		// Validate xml-file
//		DOMSource domSource = new DOMSource(document);
//		this.validator.validate(domSource);
//
//		if (this.isXMLValid(document)) {
//			Map<FollowupEvent, Integer> followupEvents = new HashMap<FollowupEvent, Integer>();
//
//			NodeList rootNodes = document.getChildNodes();
//			for (int x = 0; x < rootNodes.getLength(); x++) {
//				Node rootNode = rootNodes.item(x);
//				if (rootNode.getNodeName().equals(XMLWNEvents.ELEMENT_WNEVENTS.toString())) {
//					NodeList nodes = rootNode.getChildNodes();
//					for (int y = 0; y < nodes.getLength(); y++) {
//						Node node = nodes.item(y);
//						if (node.getNodeName().equals(XMLWNEvents.ELEMENT_PROVIDERS.toString()))
//							this.parseProviders(providerManager, node.getChildNodes());
//						else if (node.getNodeName().equals(XMLWNEvents.ELEMENT_EVENTS.toString()))
//							this.parseEvents(eventManager, providerManager, followupEvents, node.getChildNodes());
//						else if (node.getNodeName().equals(XMLWNEvents.ELEMENT_RANGES.toString()))
//							this.parseRanges(rangeManager, node.getChildNodes());
//					}
//				}
//			}
//
//			// Link every follow-up event with his event
//			for (Entry<FollowupEvent, Integer> entry : followupEvents.entrySet()) {
//				Event event = eventManager.get(entry.getValue());
//				if (event == null)
//					continue;
//
//				entry.getKey().setEvent(event);
//			}
//		}
//	}
//
//	private void parseProviders(ItemManager<Provider> providerManager, NodeList providerNodes) {
//		for (int x = 0; x < providerNodes.getLength(); x++) {
//			Node providerNode = providerNodes.item(x);
//			if (providerNode.getNodeName().equals(XMLWNEvents.ELEMENT_PROVIDER.toString())) {
//				Element providerElement = (Element) providerNode;
//
//				String id = providerElement.getAttribute(XMLWNEvents.ELEMENT_PROVIDER_ATTRIBUTE_ID.toString());
//				String name = providerElement.getAttribute(XMLWNEvents.ELEMENT_PROVIDER_ATTRIBUTE_NAME.toString());
//				String description = providerElement.getAttribute(XMLWNEvents.ELEMENT_PROVIDER_ATTRIBUTE_DESCRIPTION.toString());
//				String exportTarget = providerElement.getAttribute(XMLWNEvents.ELEMENT_PROVIDER_ATTRIBUTE_EXPORTTARGET.toString());
//				String priorityEvents = providerElement.getAttribute(XMLWNEvents.ELEMENT_PROVIDER_ATTRIBUTE_PRIORITYEVENTS.toString());
//
//				Provider provider = new Provider();
//				provider.setId(Integer.valueOf(id));
//				provider.setName(name);
//				provider.setDescription(description);
//				provider.setExportTarget(exportTarget);
//				provider.setPriorityEvents(Boolean.valueOf(priorityEvents));
//
//				providerManager.add(provider);
//			}
//		}
//	}
//
//	private void parseEvents(ItemManager<Event> eventManager, ItemManager<Provider> providerManager, Map<FollowupEvent, Integer> followupEvents, NodeList eventNodes) {
//		for (int x = 0; x < eventNodes.getLength(); x++) {
//			Node eventNode = eventNodes.item(x);
//			if (eventNode.getNodeName().equals(XMLWNEvents.ELEMENT_EVENT.toString())) {
//				Element eventElement = (Element) eventNode;
//
//				String id = eventElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ATTRIBUTE_ID.toString());
//				String type = eventElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ATTRIBUTE_TYPE.toString());
//				String responsibility = eventElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ATTRIBUTE_RESPONSIBILITY.toString());
//				String bmCritical = eventElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ATTRIBUTE_BMCRITICAL.toString());
//
//				Event event = new Event();
//				event.setId(Integer.valueOf(id));
//				event.setType(Type.valueOf(type.toUpperCase()));
//				event.setResponsibility(responsibility);
//				event.setBMCirtical(Boolean.valueOf(bmCritical));
//
//				NodeList nodes = eventElement.getChildNodes();
//				for (int y = 0; y < nodes.getLength(); y++) {
//					Node node = nodes.item(y);
//					if (node.getNodeName().equals(XMLWNEvents.ELEMENT_EVENT_ELEMENT_SETDESCRIPTION.toString()))
//						event.setSetDescription(node.getTextContent());
//					else if (node.getNodeName().equals(XMLWNEvents.ELEMENT_EVENT_ELEMENT_RESETDESCRIPTION.toString()))
//						event.setResetDescription(node.getTextContent());
//					else if (node.getNodeName().equals(XMLWNEvents.ELEMENT_EVENT_ELEMENT_TEXTS.toString()))
//						this.parseEventTexts(event, node.getChildNodes());
//					else if (node.getNodeName().equals(XMLWNEvents.ELEMENT_EVENT_ELEMENT_EUROPAYEVENT.toString()))
//						this.parseEuropayEvent(event, node);
//					else if (node.getNodeName().equals(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAPPING.toString()))
//						this.parseEventMapping(event, providerManager, followupEvents, node.getChildNodes());
//				}
//
//				eventManager.add(event);
//			}
//		}
//	}
//
//	private void parseEventTexts(Event event, NodeList textNodes) {
//		for (int x = 0; x < textNodes.getLength(); x++) {
//			Node textNode = textNodes.item(x);
//			if (textNode.getNodeName().equals(XMLWNEvents.ELEMENT_EVENT_ELEMENT_TEXT.toString())) {
//				Element textElement = (Element) textNode;
//
//				String language = textElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_TEXT_ATTRIBUTE_LANGUAGE.toString()).toUpperCase();
//				String setText = textElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_TEXT_ATTRIBUTE_SET.toString());
//				String resetText = textElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_TEXT_ATTRIBUTE_RESET.toString());
//
//				Text eventText = new Text(event, Language.valueOf(language));
//				eventText.setSetText(setText);
//				if (!resetText.isEmpty())
//					eventText.setResetText(resetText);
//				event.addText(eventText);
//			}
//		}
//	}
//
//	private void parseEuropayEvent(Event event, Node europayEventNode) {
//		Element europayEventElement = (Element) europayEventNode;
//
//		String set = europayEventElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_EUROPAYEVENT_ATTRIBUTE_SET.toString());
//		String reset = europayEventElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_EUROPAYEVENT_ATTRIBUTE_RESET.toString());
//
//		EuropayEvent europayEvent = new EuropayEvent(event);
//		if (!set.isEmpty())
//			europayEvent.setSetId(Integer.valueOf(set));
//		if (!reset.isEmpty())
//			europayEvent.setResetId(Integer.valueOf(reset));
//		event.setEuropayEvent(europayEvent);
//	}
//
//	private void parseEventMapping(Event event, ItemManager<Provider> providerManager, Map<FollowupEvent, Integer> followupEvents, NodeList mapNodes) {
//		for (int x = 0; x < mapNodes.getLength(); x++) {
//			Node mapNode = mapNodes.item(x);
//			if (mapNode.getNodeName().equals(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP.toString())) {
//				Element mapElement = (Element) mapNode;
//
//				String providerId = mapElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP_ATTRIBUTE_PROVIDER.toString());
//				String set = mapElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP_ATTRIBUTE_SET.toString());
//				String reset = mapElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP_ATTRIBUTE_RESET.toString());
//				String priorityEvent = mapElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP_ATTRIBUTE_PRIORITYEVENT.toString());
//
//				Provider provider = providerManager.get(Integer.valueOf(providerId));
//				if (provider == null)
//					continue;
//
//				Mapping eventMapping = new Mapping(event, provider);
//				event.addMapping(eventMapping);
//
//				if (!set.isEmpty())
//					eventMapping.setSetId(Integer.valueOf(set));
//				if (!reset.isEmpty())
//					eventMapping.setResetId(Integer.valueOf(reset));
//				if (!priorityEvent.isEmpty())
//					eventMapping.setPriorityEvent(Boolean.valueOf(priorityEvent));
//
//				NodeList nodes = mapElement.getChildNodes();
//				for (int y = 0; y < nodes.getLength(); y++) {
//					Node node = nodes.item(y);
//					if (node.getNodeName().equals(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP_ELEMENT_FOLLOWUPEVENT.toString()))
//						this.parseFollowupEvent(eventMapping, followupEvents, node);
//					else if (node.getNodeName().equals(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP_ELEMENT_OUTPUT.toString()))
//						this.parseEventOutputs(eventMapping, node);
//					else if (node.getNodeName().equals(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP_ELEMENT_RELAY.toString()))
//						this.parseEventRelays(eventMapping, node);
//					else if (node.getNodeName().equals(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP_ELEMENT_LED.toString()))
//						this.parseEventLeds(eventMapping, node);
//				}
//			}
//		}
//	}
//
//	private void parseFollowupEvent(Mapping eventMapping, Map<FollowupEvent, Integer> followupEvents, Node followupEventNode) {
//		Element followupEventElement = (Element) followupEventNode;
//
//		String event = followupEventElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP_ELEMENT_FOLLOWUPEVENT_ATTRIBUTE_EVENT.toString());
//		String period = followupEventElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP_ELEMENT_FOLLOWUPEVENT_ATTRIBUTE_PERIOD.toString());
//		String limit = followupEventElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP_ELEMENT_FOLLOWUPEVENT_ATTRIBUTE_LIMIT.toString());
//
//		FollowupEvent followupEvent = new FollowupEvent();
//		followupEvent.setPeriod(Integer.valueOf(period));
//		followupEvent.setLimit(Integer.valueOf(limit));
//		eventMapping.setFollowupEvent(followupEvent);
//
//		// Temporarily save the follow-up events
//		// Can not yet link with their event, because not all events are loaded
//		followupEvents.put(followupEvent, Integer.valueOf(event));
//	}
//
//	private void parseEventOutputs(Mapping eventMapping, Node outputNode) {
//		Element outputElement = (Element) outputNode;
//		String name = outputElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP_ELEMENT_OUTPUT_ATTRIBUTE_NAME.toString());
//
//		Output eventOutput = Output.valueOf(name.toUpperCase());
//		eventMapping.addOutput(eventOutput);
//	}
//
//	private void parseEventRelays(Mapping eventMapping, Node relayNode) {
//		Element relayElement = (Element) relayNode;
//		String name = relayElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP_ELEMENT_RELAY_ATTRIBUTE_NAME.toString());
//
//		Relay eventRelay = Relay.valueOf(name.toUpperCase());
//		eventMapping.addRelay(eventRelay);
//	}
//
//	private void parseEventLeds(Mapping eventMapping, Node ledNode) {
//		Element ledElement = (Element) ledNode;
//		String name = ledElement.getAttribute(XMLWNEvents.ELEMENT_EVENT_ELEMENT_MAP_ELEMENT_LED_ATTRIBUTE_NAME.toString());
//
//		Led eventLed = Led.valueOf(name.toUpperCase());
//		eventMapping.addLed(eventLed);
//	}
//
//	private void parseRanges(ItemManager<Range> rangeManager, NodeList rangeNodes) {
//		for (int x = 0; x < rangeNodes.getLength(); x++) {
//			Node rangeNode = rangeNodes.item(x);
//			if (rangeNode.getNodeName().equals(XMLWNEvents.ELEMENT_RANGE.toString())) {
//				Element rangeElement = (Element) rangeNode;
//
//				String id = rangeElement.getAttribute(XMLWNEvents.ELEMENT_RANGE_ATTRIBUTE_ID.toString());
//				String name = rangeElement.getAttribute(XMLWNEvents.ELEMENT_RANGE_ATTRIBUTE_NAME.toString());
//				String start = rangeElement.getAttribute(XMLWNEvents.ELEMENT_RANGE_ATTRIBUTE_START.toString());
//				String end = rangeElement.getAttribute(XMLWNEvents.ELEMENT_RANGE_ATTRIBUTE_END.toString());
//
//				Range range = new Range();
//				range.setId(Integer.valueOf(id));
//				range.setName(name);
//				range.setStart(Integer.valueOf(start));
//				range.setEnd(Integer.valueOf(end));
//				rangeManager.add(range);
//			}
//		}
//	}
//
// }
