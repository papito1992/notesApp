import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntity, updateEntity, createEntity, reset } from './note.reducer';
import { INote } from 'app/shared/model/note.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const NoteUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const account = useAppSelector(state => state.authentication.account);
  const noteEntity = useAppSelector(state => state.note.entity);
  const loading = useAppSelector(state => state.note.loading);
  const updating = useAppSelector(state => state.note.updating);
  const updateSuccess = useAppSelector(state => state.note.updateSuccess);

  const handleClose = () => {
    props.history.push('/note');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.expirationDate = convertDateTimeToServer(values.expirationDate);

    const entity = {
      ...noteEntity,
      ...values,
      user: { id: account.id, login: account.login.toString() },
    };
    // console.log({
    //   "id": 1,
    //   "login": "admin"
    // })
    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          expirationDate: displayDefaultDateTime(),
        }
      : {
          ...noteEntity,
          expirationDate: convertDateTimeFromServer(noteEntity.expirationDate),
          userId: noteEntity?.user?.id,
        };
  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="shareNotesApp.note.home.createOrEditLabel" data-cy="NoteCreateUpdateHeading">
            Create or edit a Note
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="note-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Content"
                id="note-content"
                name="content"
                data-cy="content"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 5, message: 'This field is required to be at least 5 characters.' },
                  maxLength: { value: 150, message: 'This field cannot be longer than 150 characters.' },
                }}
              />
              <ValidatedField
                label="Password"
                id="note-password"
                name="password"
                data-cy="password"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  minLength: { value: 5, message: 'This field is required to be at least 5 characters.' },
                  maxLength: { value: 20, message: 'This field cannot be longer than 20 characters.' },
                }}
              />
              <ValidatedField
                label="Expiration Date"
                id="note-expirationDate"
                name="expirationDate"
                data-cy="expirationDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/note" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default NoteUpdate;
