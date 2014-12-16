package engine.core.asset;

import engine.core.exceptions.AssetException;
import engine.imp.render.Animation;
import engine.imp.render.Frame;
import engine.imp.render.Material2D;
import engine.imp.render.RenderingException;
import glcommon.Color;
import gltools.gl.GL;
import gltools.texture.Texture2D;
import gltools.texture.TextureFactory;
import gltools.texture.TextureWrapMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import commons.GLResourceLocator;
import commons.Logger;
import commons.Resource;
import commons.ResourceException;
import commons.ResourceLocator;

/**
 * Contains methods for getting resources.
 */
public class ResourceFactory {
	private ResourceFactory() {

	}

	/**
	 * @param locator
	 * @param resource
	 * @return the read String
	 */
	public static String readString(Resource resource) {
		if (resource == null)
			throw new ResourceException("Cannot read a null resource!");
		try {
			StringBuilder text = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(resource.open()));
			String line;
			while ((line = reader.readLine()) != null) {
				text.append(line).append('\n');
			}
			reader.close();

			return text.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Makes a Texture2D from a Resource.
	 * 
	 * @param gl
	 * @param resource
	 * @return
	 */
	public static Texture2D createTexture(GL gl, Resource resource) {
		try {
			Texture2D texture = TextureFactory.s_loadTexture(gl, resource.getResource(),
					new GLResourceLocator(resource.getLocator()));
			texture.bind(gl);
			texture.setTWrapMode(TextureWrapMode.REPEAT);
			texture.setSWrapMode(TextureWrapMode.REPEAT);
			texture.loadParams(gl);
			texture.unbind(gl);
			return texture;
		} catch (IOException e) {
			throw new RenderingException("Material could not be created: " + resource.getResource(), e);
		}
	}

	/**
	 * Reads a Material2D out of a JSoup Element.
	 * 
	 * @param element
	 * @param gl
	 * @param locator
	 * @return
	 */
	public static Material2D readMaterial(Element element, GL gl, ResourceLocator locator) {
		Material2D mat = new Material2D();
		if (element.hasAttr("texture")) {
			String texture = element.attr("texture");
			mat.setTexture(createTexture(gl, new Resource(locator, texture)));
		}
		if (element.hasAttr("normalmap")) {
			String normal = element.attr("normalmap");
			mat.setTexture(createTexture(gl, new Resource(locator, normal)));
		}
		if (element.hasAttr("color")) {
			String color = element.attr("color");
			String noWhitespace = color.replaceAll("\\s+", "");
			String[] rgb = noWhitespace.split(",");
			if (rgb.length != 3)
				throw new AssetException("A color must have 3 components: " + color);
			mat.setColor(new Color(Float.parseFloat(rgb[0]), Float.parseFloat(rgb[1]), Float.parseFloat(rgb[2])));
		}
		if (element.hasAttr("lighted")) {
			String lighted = element.attr("lighted");
			if (lighted.equals("true")) {
				mat.setLighted(true);
			} else if (lighted.equals("false")) {
				mat.setLighted(false);
			} else {
				throw new AssetException("Lighted must be true or false: " + lighted);
			}
		}
		return mat;
	}

	/**
	 * Reads an Animation out of a JSoup Element.
	 * 
	 * @param animation
	 * @param gl
	 * @param locator
	 * @return
	 */
	public static Animation readAnimation(Element animation, GL gl, ResourceLocator locator) {
		List<Frame> frameList = new ArrayList<Frame>();
		boolean repeat = false;

		if (animation.hasAttr("repeat")) {
			String repeatAttr = animation.attr("repeat");
			if (repeatAttr.equals("true")) {
				repeat = true;
			} else if (repeatAttr.equals("false")) {
				repeat = false;
			} else {
				Logger.instance().error("Not a legal repeat flag: " + repeatAttr);
			}
		}

		Elements frames = animation.getElementsByTag("frame");
		for (Element e : frames) {
			Element material = e.getElementsByTag("material").first();
			float duration = Float.parseFloat(e.attr("duration"));
			Material2D mat2d = ResourceFactory.readMaterial(material, gl, locator);

			frameList.add(new Frame(mat2d, duration));
		}

		Animation anim = new Animation(frameList);
		anim.setRepeating(repeat);

		return anim;
	}
}
