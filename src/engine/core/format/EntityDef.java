package engine.core.format;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Collections;

import engine.core.Entity;
import engine.core.EntityBuilder;
import engine.core.Scene;
import engine.core.TreeNode;


public class EntityDef {
	private List<String> m_parentPath = new ArrayList<String>();
	private EntityBuilder m_builder;
	private String m_name;
	
	public EntityDef() {}
	
	public EntityDef(Entity e) {
		m_name = e.getName();
		m_builder = e.getBuilder();
		
		//Assemble the path
		TreeNode parent = e.tree().getParent();
		while(!(parent instanceof Scene)) {
			m_parentPath.add(parent.getName());
			parent = ((Entity) parent).tree().getParent();
		}
		Collections.reverse(m_parentPath);
	}
	
	
	public void setPath(List<String> path) { m_parentPath = path; }
	public void setName(String name) { m_name = name; }
	public void setBuilder(EntityBuilder builder) { m_builder = builder; }
	
	public List<String> getParentPath() { return m_parentPath; }
	public EntityBuilder getBuilder() { return m_builder; }
	public String getName() { return m_name; }
}
