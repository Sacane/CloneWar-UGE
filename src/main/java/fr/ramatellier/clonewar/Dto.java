package fr.ramatellier.clonewar;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface Dto<T> {

    T toEntity();
}
