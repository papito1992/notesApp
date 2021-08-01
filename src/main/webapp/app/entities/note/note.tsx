import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getUsers, getUser } from 'app/modules/administration/user-management/user-management.reducer';
import { getEntities } from './note.reducer';
import { INote } from 'app/shared/model/note.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Note = (props: RouteComponentProps<{ url: string }>) => {
  const account = useAppSelector(state => state.authentication.account);
  const dispatch = useAppDispatch();

  const noteList = useAppSelector(state => state.note.entities);
  const loading = useAppSelector(state => state.note.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;
  console.log(account);
  return (
    <div>
      <h2 id="note-heading" data-cy="NoteHeading">
        Notes of {account.email}
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Note
          </Link>
        </div>
      </h2>
      <h6>Link is generated automatically</h6>
      <div className="table-responsive">
        {noteList && noteList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Content</th>
                <th>Password</th>
                <th>Link</th>
                <th>Expiration Date</th>
                <th>User</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {noteList.map((note, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${note.id}`} color="link" size="sm">
                      {note.id}
                    </Button>
                  </td>
                  <td>{note.content}</td>
                  <td>{note.password}</td>
                  <td>{note.link}</td>
                  <td>{note.expirationDate ? <TextFormat type="date" value={note.expirationDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{note.user ? note.user.login : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${note.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${note.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${note.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Notes found</div>
        )}
      </div>
    </div>
  );
};

export default Note;
