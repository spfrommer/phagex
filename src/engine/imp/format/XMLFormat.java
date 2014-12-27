package engine.imp.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

import commons.StringUtils;
import commons.Transform2f;
import commons.matrix.Vector2f;

import engine.core.CScriptData;
import engine.core.CTags;
import engine.core.CTransform;
import engine.core.Component;
import engine.core.ComponentBuilder;
import engine.core.EntityBuilder;
import engine.core.TagList;
import engine.core.asset.AssetManager;
import engine.core.format.Decoder;
import engine.core.format.Encoder;
import engine.core.format.EntityDef;
import engine.core.format.Format;
import engine.core.format.MetaDecoder;
import engine.core.format.MetaEncoder;
import engine.core.script.XScript;

public class XMLFormat extends Format<String, EntityDef, EntityDef> {
	private ArrayList<Encoder<Component, String>> m_encoders = new ArrayList<Encoder<Component, String>>();
	private ArrayList<Decoder<Element, Component>> m_decoders = new ArrayList<Decoder<Element, Component>>();

	private ArrayList<MetaEncoder<EntityDef, String>> m_metaEncoders = new ArrayList<MetaEncoder<EntityDef, String>>();
	private ArrayList<MetaDecoder<Element, EntityDef>> m_metaDecoders = new ArrayList<MetaDecoder<Element, EntityDef>>();

	public XMLFormat() {
	}

	public void addEncoder(Encoder<Component, String> encoder) {
		m_encoders.add(encoder);
	}

	public void addDecoder(Decoder<Element, Component> decoder) {
		m_decoders.add(decoder);
	}

	public void addMetaEncoder(MetaEncoder<EntityDef, String> encoder) {
		m_metaEncoders.add(encoder);
	}

	public void addMetaDecoder(MetaDecoder<Element, EntityDef> decoder) {
		m_metaDecoders.add(decoder);
	}

	public void removeEncoder(Encoder<Component, String> encoder) {
		m_encoders.remove(encoder);
	}

	public void removeDecoder(Decoder<Element, Component> decoder) {
		m_decoders.remove(decoder);
	}

	public void removeMetaEncoder(MetaEncoder<EntityDef, String> encoder) {
		m_metaEncoders.remove(encoder);
	}

	public void removeMetaDecoder(MetaDecoder<Element, EntityDef> decoder) {
		m_metaDecoders.remove(decoder);
	}

	@Override
	public boolean isEncodable(EntityDef entity) {
		return true;
	}

	@Override
	public boolean isDecodable(String s) {
		Document doc = Jsoup.parse(s, "", Parser.xmlParser());
		return doc.tagName().equals("entity");
	}

	@Override
	public String encode(EntityDef entity) {
		return create(entity, true);
	}

	public String create(EntityDef entity, boolean specifyParent) {
		StringBuilder xml = new StringBuilder();

		xml.append("<entity ").append("name=\"").append(entity.getName()).append("\"");
		if (specifyParent) {
			List<String> names = new ArrayList<String>(entity.getParentPath());
			if (names.size() != 0) {
				xml.append(' ').append("parent=\"");
				xml.append(StringUtils.join(".", names));
				xml.append("\"");
			}
		}
		xml.append(">").append('\n');

		for (MetaEncoder<EntityDef, String> metacoder : m_metaEncoders) {
			if (metacoder.isEncodable(entity))
				xml.append(metacoder.encode(entity)).append('\n');
		}

		xml.append("<components>").append('\n');
		for (ComponentBuilder<? extends Component> c : entity.getBuilder().getComponentBuilders()) {
			Component comp = c.build();
			for (Encoder<Component, String> enc : m_encoders) {
				if (enc.isEncodable(comp)) {
					xml.append(enc.encode(comp)).append('\n');
					break;
				}
			}
		}
		xml.append("</components>").append('\n');

		xml.append("<children>").append('\n');

		for (Entry<String, EntityBuilder> e : entity.getBuilder().getEntityBuilders().entrySet()) {
			EntityDef def = new EntityDef();
			def.setName(e.getKey());
			def.setBuilder(e.getValue());
			xml.append(create(def, false)).append('\n');
		}

		xml.append("</children>").append('\n');

		xml.append("</entity>").append('\n');

		return xml.toString();
	}

	@Override
	public EntityDef decode(String value) {
		Document doc = Jsoup.parse(value, "", Parser.xmlParser());
		Element root = doc;
		return parse(root);
	}

	@SuppressWarnings("unchecked")
	private EntityDef parse(Element e) {
		System.out.println(e);
		EntityDef d = new EntityDef();
		d.setName(e.attr("name"));
		String parent = e.attr("");
		if (!parent.equals("")) {
			d.setPath(Arrays.asList(parent.split("\\.")));
		}
		EntityBuilder b = new EntityBuilder();
		d.setBuilder(b);

		for (Element child : e.children()) {
			if (child.tagName().equals("components")) {
				for (Element el : child.children()) {
					for (Decoder<Element, Component> dec : m_decoders) {
						if (dec.isDecodable(el)) {
							b.addComponentBuilder(dec.decode(el));
							break;
						}
					}
				}
			} else if (child.tagName().equals("children")) {
				for (Element el : child.children()) {
					EntityDef def = parse(el);
					b.addChildBuilder(def.getName(), def.getBuilder());
				}
			} else {
				for (MetaDecoder<Element, EntityDef> dec : m_metaDecoders) {
					if (dec.isDecodable(e)) {
						dec.decode(child, d);
						break;
					}
				}
			}
		}

		return d;
	}

	public static class XMLScriptDecoder implements MetaDecoder<Element, EntityDef> {
		@Override
		public boolean isDecodable(Element value) {
			return value.tagName().equals("scripts");
		}

		@Override
		public void decode(Element value, EntityDef object) {
			if (!isDecodable(value))
				throw new IllegalArgumentException("Cannot decode: " + value);
			Elements children = value.children();
			for (Element s : children) {
				XScript script = AssetManager.instance().get(s.attr("asset"), XScript.class);
				object.getBuilder().addScript(script);
			}
		}
	}

	public static class XMLScriptEncoder implements MetaEncoder<EntityDef, String> {
		@Override
		public boolean isEncodable(EntityDef object) {
			return true;
		}

		@Override
		public String encode(EntityDef object) {
			StringBuilder builder = new StringBuilder();

			List<XScript> scripts = object.getBuilder().getScripts();

			builder.append("<scripts>\n");

			for (XScript script : scripts) {
				System.out.println(script.getIdentifier());
				builder.append("<script asset=\"").append(script.getIdentifier()).append("\"/>\n");
			}

			builder.append("<scripts/>");

			return builder.toString();
		}
	}

	public static class XMLTransformDecoder implements Decoder<Element, Component> {
		@Override
		public boolean isDecodable(Element value) {
			return value.tagName().equals("transform");
		}

		@Override
		public Component decode(Element value) {
			if (!isDecodable(value))
				throw new IllegalArgumentException("Cannot decode: " + value);
			Vector2f trans = new Vector2f(Float.parseFloat(value.attr("x")), Float.parseFloat(value.attr("y")));
			Vector2f scale = new Vector2f(Float.parseFloat(value.attr("sx")), Float.parseFloat(value.attr("sy")));
			float rot = Float.parseFloat(value.attr("rot"));

			return new CTransform(new Transform2f(trans, rot, scale));
		}
	}

	public static class XMLTransformEncoder implements Encoder<Component, String> {
		@Override
		public boolean isEncodable(Component object) {
			return object instanceof CTransform;
		}

		@Override
		public String encode(Component object) {
			if (object instanceof CTransform) {
				CTransform transComp = (CTransform) object;

				Vector2f trans = transComp.getTranslation();
				Vector2f scale = transComp.getScale();
				float rot = transComp.getRotation();

				StringBuilder builder = new StringBuilder();

				builder.append("<transform x=\"").append(trans.getX()).append("\" y=\"").append(trans.getY()).append("\" ");
				builder.append("rot=\"").append(rot).append("\" ");
				builder.append("sx=\"").append(scale.getX()).append("\" sy=\"").append(scale.getY()).append("\"/>");

				return builder.toString();
			} else
				throw new IllegalArgumentException("Must pass in CTransform!");
		}
	}

	public static class XMLTagsDecoder implements Decoder<Element, Component> {
		@Override
		public boolean isDecodable(Element value) {
			return value.tagName().equals("tags");
		}

		@Override
		public Component decode(Element value) {
			if (!isDecodable(value))
				throw new IllegalArgumentException("Cannot decode: " + value);
			String text = value.text();
			String[] tags = text.split("::");
			return new CTags(new TagList(tags));
		}
	}

	public static class XMLTagsEncoder implements Encoder<Component, String> {
		@Override
		public boolean isEncodable(Component object) {
			return object instanceof CTags;
		}

		@Override
		public String encode(Component object) {
			if (object instanceof CTags) {
				CTags tags = (CTags) object;
				TagList tagList = tags.getTags();
				StringBuilder builder = new StringBuilder();
				builder.append("<tags>");
				Iterator<String> it = tagList.iterator();
				while (it.hasNext()) {
					String tag = it.next();
					builder.append(tag);
					if (it.hasNext())
						builder.append("::");
				}

				builder.append("</tags>");
				return builder.toString();
			} else
				throw new IllegalArgumentException("Must pass in CTransform!");
		}
	}

	public static class XMLScriptDataDecoder implements Decoder<Element, Component> {
		private HashMap<Class<?>, Decoder<String, ?>> m_dataDecoders = new HashMap<Class<?>, Decoder<String, ?>>();

		public void addDataDecoder(Class<?> type, Decoder<String, ?> encoder) {
			m_dataDecoders.put(type, encoder);
		}

		@Override
		public boolean isDecodable(Element value) {
			return value.tagName().equals("scriptData");
		}

		@Override
		public Component decode(Element value) {
			if (!isDecodable(value))
				throw new IllegalArgumentException("Cannot decode: " + value);
			CScriptData data = new CScriptData();
			for (Element var : value.children()) {
				Class<?> type = null;
				try {
					type = Class.forName(var.attr("type"));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				Decoder<String, ?> decoder = m_dataDecoders.get(type);
				if (decoder != null) {
					data.setData(var.tagName(), decoder.decode(var.text()));
				} else
					throw new RuntimeException("Cannot decode: " + value);
			}
			return data;
		}
	}

	public static class XMLScriptDataEncoder implements Encoder<Component, String> {
		private HashMap<Class<?>, Encoder<Object, String>> m_dataEncoders = new HashMap<Class<?>, Encoder<Object, String>>();

		private Encoder<Object, String> m_defaultEncoder = null;

		public XMLScriptDataEncoder() {
			// Set the default encoder
			m_defaultEncoder = new Encoder<Object, String>() {
				@Override
				public boolean isEncodable(Object object) {
					return true;
				}

				@Override
				public String encode(Object object) {
					return object.toString();
				}
			};
		}

		public void addDataEncoder(Class<?> clazz, Encoder<Object, String> encoder) {
			m_dataEncoders.put(clazz, encoder);
		}

		@Override
		public boolean isEncodable(Component object) {
			return object instanceof CScriptData;
		}

		@Override
		public String encode(Component object) {
			if (!isEncodable(object))
				throw new IllegalArgumentException("Cannot decode: " + object);
			CScriptData scriptData = (CScriptData) object;

			StringBuilder builder = new StringBuilder();
			builder.append("<scriptData>\n");
			String[] idents = scriptData.getIdentifiers();
			for (String var : idents) {
				Object val = scriptData.getData(var);
				String encoded = encodeData(val);
				builder.append("<").append(var).append(" type=\"").append(val.getClass().getName()).append("\"/>\n");
				builder.append(encoded);
				builder.append("</").append(var).append(">\n");
			}
			builder.append("</scriptData>");
			return builder.toString();
		}

		public String encodeData(Object val) {
			Encoder<Object, String> m_encoder = m_dataEncoders.get(val.getClass());
			if (m_encoder != null && m_encoder.isEncodable(val)) {
				return m_encoder.encode(val);
			} else if (m_defaultEncoder != null && m_defaultEncoder.isEncodable(val)) {
				return m_defaultEncoder.encode(val);
			} else
				throw new RuntimeException("Cannot encode: " + val);
		}
	}

}
