/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package org.syncope.core.persistence.validation.entity;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.syncope.core.persistence.beans.AbstractAttr;
import org.syncope.types.EntityViolationType;

public class AttrValidator
        implements ConstraintValidator<AttrCheck, AbstractAttr> {

    @Override
    public void initialize(final AttrCheck constraintAnnotation) {
    }

    @Override
    public boolean isValid(final AbstractAttr object,
            final ConstraintValidatorContext context) {

        boolean isValid;

        if (object == null) {
            isValid = true;
        } else {
            if (object.getSchema().isUniqueConstraint()) {
                isValid = object.getValues().isEmpty()
                        && object.getUniqueValue() != null;
            } else {
                isValid = !object.getValues().isEmpty()
                        && object.getUniqueValue() == null;

                if (!object.getSchema().isMultivalue()) {
                    isValid &= object.getValues().size() == 1
                            && object.getUniqueValue() == null;
                }
            }

            if (!isValid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        EntityViolationType.InvalidValueList.toString()).
                        addConstraintViolation();
            }
        }

        return isValid;
    }
}
