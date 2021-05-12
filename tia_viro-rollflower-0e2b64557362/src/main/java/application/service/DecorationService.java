package application.service;

import java.util.List;

import application.model.Decoration;

public interface DecorationService {
	List<Decoration> getAllDecorations();

	Decoration getDecorationById(Long id);

	void addDecoration(Decoration decoration);

	void updateDecoration(Decoration decoration);

	void deleteDecoration(Long id);

	boolean decorationExists(Decoration decoration);
}
