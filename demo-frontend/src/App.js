import Home from './pages/Home';
import Login from "./pages/Login";
import Register from "./pages/Register"

import Layout from './pages/Layout';
import LinkPage from './pages/LinkPage';
import Unauthorized from './pages/Unauthorized';
import Lounge from './pages/Lounge';
import Missing from './pages/Missing';
import Editor from './pages/Editor';
import Admin from './pages/Admin';
import RequireAuth from './pages/RequireAuth';
import { Routes, Route } from 'react-router-dom';

function App() {
  return (
    <main className="App">
      <Routes>
        <Route path="/" element={<Layout />}>
          {/* public routes */}
          <Route path="login" element={<Login />} />
          <Route path="register" element={<Register />} />
          <Route path="LinkPage" element={<LinkPage />} />
          <Route path="unauthorized" element={<Unauthorized />} />

          {/* protected routes */}
          <Route element={<RequireAuth allowedRoles={["ROLE_USER"]} />}>
            <Route path="/" element={<Home />} />
          </Route>

          <Route element={<RequireAuth allowedRoles={["ROLE_MANAGER"]} />}>
            <Route path="editor" element={<Editor />} />
          </Route>

          <Route element={<RequireAuth allowedRoles={["ROLE_ADMIN"]} />}>
            <Route path="admin" element={<Admin />} />
          </Route>
          <Route element={<RequireAuth allowedRoles={["ROLE_MANAGER", "ROLE_ADMIN"]} />}>
            <Route path="lounge" element={<Lounge />} />
          </Route>

          {/* catch all */}
          <Route path="*" element={<Missing />} />
        </Route>
      </Routes>
    </main>
  );
}

export default App;
