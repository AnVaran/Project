package application.service;

import java.util.List;

import application.model.Wrapper;

public interface WrapperService {
	List<Wrapper> getAllWrappers();

	Wrapper getWrapperById(Long id);

	void addWrapper(Wrapper wrapper);

	void updateWrapper(Wrapper wrapper);

	void deleteWrapper(Long id);

	boolean wrapperExists(Wrapper wrapper);
}
