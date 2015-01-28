package core.application;

import java.util.Arrays;
import java.util.List;

import api.application.StaticData;
import api.application.request.Degree;
import api.application.request.SchoolType;

public class StaticDataService implements StaticData {
	
	@Override
	public List<Degree> getDegrees() {
		return Arrays.asList(Degree.values()); 
	}

	@Override
	public List<SchoolType> getSchoolTypes() {
		return Arrays.asList(SchoolType.values());
	}
}
