package engine.core.format;

import java.util.ArrayList;
import java.util.List;

import engine.core.EntityBuilder;

public class EntityDef {
	private List<String> m_parentPath = new ArrayList<String>();

	private EntityBuilder m_builder;
	private String m_name;
	
	
	public void setPath(List<String> path) { m_parentPath = path; }
	public void setName(String name) { m_name = name; }
	public void setBuilder(EntityBuilder builder) { m_builder = builder; }
	
	public List<String> getParentPath() { return m_parentPath; }
	public EntityBuilder getBuilder() { return m_builder; }
	public String getName() { return m_name; }
}
