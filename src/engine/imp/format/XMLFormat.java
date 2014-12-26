package engine.imp.format;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Collections;

import commons.StringUtils;
import commons.Transform2f;
import commons.matrix.Vector2f;

import engine.core.CTransform;
import engine.core.Component;
import engine.core.Entity;
import engine.core.EntityBuilder;
import engine.core.Scene;
import engine.core.TreeNode;
import engine.core.asset.AssetManager;
import engine.core.format.Decoder;
import engine.core.format.Encoder;
import engine.core.format.EntityDef;
import engine.core.format.Format;
import engine.core.format.MetaDecoder;
import engine.core.format.MetaEncoder;
import engine.core.script.XScript;
import engine.core.script.XScriptTypeManager;

public class XMLFormat extends Format<String, Entity, EntityDef> {
	private ArrayList<Encoder<Component, String>> m_encoders = new ArrayList<Encoder<Component, String>>();
	private ArrayList<Decoder<Element, Component>> m_decoders = new ArrayList<Decoder<Element, Component>>();
	
	private ArrayList<MetaEncoder<Entity, String>> m_metaEncoders = new ArrayList<MetaEncoder<Entity, String>>();
	private ArrayList<MetaDecoder<Element, EntityDef>> m_metaDecoders = new ArrayList<MetaDecoder<Element, EntityDef>>();
	
	public XMLFormat() {}
	
	public void addEncoder(Encoder<Component, String> encoder) { m_encoders.add(encoder); }
	public void addDecoder(Decoder<Element, Component> decoder) { m_decoders.add(decoder); }
	public void addMetaEncoder(MetaEncoder<Entity, String> encoder) { m_metaEncoders.add(encoder); }
	public void addMetaDecoder(MetaDecoder<Element, EntityDef> decoder) { m_metaDecoders.add(decoder); }

	public void removeEncoder(Encoder<Component, String> encoder) { m_encoders.remove(encoder); }
	public void removeDecoder(Decoder<Element, Component> decoder) { m_decoders.remove(decoder); }
	public void removeMetaEncoder(MetaEncoder<Entity, String> encoder) { m_metaEncoders.remove(encoder); }
	public void removeMetaDecoder(MetaDecoder<Element, EntityDef> decoder) { m_metaDecoders.remove(decoder); }

	
	@Override
	public boolean isEncodable(Entity entity) {
		return true;
	}
	@Override
	public boolean isDecodable(String s) {
		Document doc = Jsoup.parse(s, "", Parser.xmlParser());
		return doc.tagName().equals("entity");
	}
	
	@Override
	public String encode(Entity entity) {
		return create(entity, true);
	}
	public String create(Entity entity, boolean specifyParent) {
		StringBuilder xml = new StringBuilder();
		
		xml.append("<entity ").append("name=\"").append(entity.getName()).append("\"");
		if (specifyParent) {
			List<String> names = new ArrayList<String>();
			TreeNode parent = entity.tree().getParent();
			while (!(parent instanceof Scene)) {
				names.add(((Entity) parent).getName());
				parent = ((Entity) parent).tree().getParent();
			}
			Collections.reverse(names);
			if (names.size() != 0) {
				System.out.println(names);
				xml.append(' ').append("parent=\"");
				xml.append(StringUtils.join(".", names));
				xml.append("\"");
			}
		}
		xml.append(">").append('\n');
		
		for (MetaEncoder<Entity, String> metacoder: m_metaEncoders) {
			if (metacoder.isEncodable(entity)) xml.append(metacoder.encode(entity)).append('\n');
		}
		
		xml.append("<components>").append('\n');
		for (Component c : entity.components().all()) {
			for (Encoder<Component, String> enc : m_encoders) {
				if (enc.isEncodable(c)) {
					xml.append(enc.encode(c)).append('\n');
					break;
				}
			}
		}
		xml.append("</components>").append('\n');
		
		xml.append("<children>").append('\n');
		
		for (Entity e : entity.tree().getChildren()) {
			xml.append(create(e, false)).append('\n');
		}
		
		xml.append("</children>").append('\n');
		
		xml.append("</entity>").append('\n');
		
		return xml.toString();		
	}

	@Override
	public EntityDef decode(String value) {
		Document doc = Jsoup.parse(value, "", Parser.xmlParser());
		Element root = (Element) doc;
		return parse(root);
	}
	
	@SuppressWarnings("unchecked")
	private EntityDef parse(Element e) {
		EntityDef d = new EntityDef();
		d.setName(e.attr("name"));
		String parent = e.attr("");
		if (!parent.equals("")) {
			d.setPath((List<String>) Arrays.asList(parent.split("\\.")));
		}
		EntityBuilder b = new EntityBuilder();
		
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
		
		return null;
	}
	public static class XMLScriptDecoder implements MetaDecoder<Element, EntityDef> {
		@Override
		public boolean isDecodable(Element value) {
			return value.tagName().equals("scripts");
		}

		@Override
		public void decode(Element value, EntityDef object) {
			if (!isDecodable(value)) throw new IllegalArgumentException("Cannot decode: " + value);
			Elements children = value.children();
			for (Element s : children) {
				XScript script = AssetManager.instance().get(s.attr("asset"), XScript.class);
				object.getBuilder().addScript(script);
			}
		}
	}
	public static class XMLScriptEncoder implements MetaEncoder<Entity, String> {
		@Override
		public boolean isEncodable(Entity object) {
			return true;
		}
		@Override
		public String encode(Entity object) {
			StringBuilder builder = new StringBuilder();
			
			List<XScript> scripts = object.scripts().getAllScripts();
			
			builder.append("<scripts>\n");
			
			for (XScript script : scripts) {
				System.out.println(script.getIdentifier());
				builder.append("<script asset=\"").append(script.getIdentifier()).append("\">\n");
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
			if (!isDecodable(value)) throw new IllegalArgumentException("Cannot decode: " + value);
			Vector2f trans = new Vector2f(Float.parseFloat(value.attr("x")),
					  Float.parseFloat(value.attr("y")));
			Vector2f scale = new Vector2f(Float.parseFloat(value.attr("sx")),
										  Float.parseFloat(value.attr("sy")));
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
				Transform2f transform = transComp.getTransform();
				
				Vector2f trans = transform.getTranslation();
				Vector2f scale = transform.getScale();
				float rot = transform.getRotation();
				
				StringBuilder builder = new StringBuilder();
				
				builder.append("<transform x=\"").append(trans.getX())
					   .append("\" y=\"").append(trans.getY()).append("\" ");
				builder.append("rot=\"").append(rot).append("\" ");
				builder.append("sx=\"").append(scale.getX())
					   .append("\" sy=\"").append(scale.getY()).append("\"/>");
				
				return builder.toString();
			} else throw new IllegalArgumentException("Must pass in CTransform!");
		}
	}
	
}
