package lt.ehu.student.moneytrackerbeta.dao;
import lt.ehu.student.moneytrackerbeta.exception.DaoException;
import lt.ehu.student.moneytrackerbeta.model.AbstractModel;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface BaseDao<T extends AbstractModel> {
    boolean create(T t) throws DaoException, SQLException;
    boolean update(T t) throws DaoException;
    boolean delete(T t) throws DaoException;
    List<T> findAll() throws DaoException, SQLException;
    T findById(int id) throws DaoException;
    T findById(UUID id) throws DaoException;
}
