import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import Courses from "./components/Courses";
import CourseList from "./components/CourseList";
import CourseDetail from "./components/CourseDetail";

function App() {
  return (
<Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/courses" element={<CourseList />} />
        <Route path="/courses/:id" element={<CourseDetail />} />
      </Routes>
    </Router>
  );
}

export default App;
